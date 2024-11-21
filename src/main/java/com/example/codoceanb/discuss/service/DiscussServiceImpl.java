package com.example.codoceanb.discuss.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.discuss.dto.CategoryDTO;
import com.example.codoceanb.discuss.entity.Category;
import com.example.codoceanb.discuss.entity.Discuss;
import com.example.codoceanb.discuss.entity.Image;
import com.example.codoceanb.discuss.repository.CategoryRepository;
import com.example.codoceanb.discuss.repository.DiscussRepository;
import com.example.codoceanb.discuss.request.AddDiscussRequest;
import com.example.codoceanb.discuss.request.UpdateDiscussRequest;
import com.example.codoceanb.discuss.response.DiscussResponse;
import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.search.dto.SearchResultDTO;
import com.example.codoceanb.search.service.SearchServiceImpl;
import com.example.codoceanb.uploadfile.service.UploadFileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DiscussServiceImpl implements DiscussService{
    private static final Logger log = LogManager.getLogger(SearchServiceImpl.class);

    @Autowired
    private DiscussRepository discussRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public List<DiscussDTO> getAllUploadedDiscussesByUser(String token) {
        try {
            User user = userService.getUserDetailsFromToken(token);
            String email = user.getEmail();
            List<Discuss> discusses = discussRepository.findByOwnerEmail(email);
            return discusses.stream()
                    .map(discuss -> discuss.toDTO(user.getId()))
                    .collect(Collectors.toList());
        } catch (io.jsonwebtoken.io.DecodingException e) {
            log.error("Error decoding token: ", e);
            throw new IllegalArgumentException("Invalid token");
        } catch (Exception e) {
            log.error("Error retrieving discussions by owner: ", e);
            throw new RuntimeException("Unable to retrieve discussions");
        }
    }

    @Override
    public DiscussResponse getDiscusses(String authHeader,
                                        int pageNumber,
                                        int limit,
                                        String searchTerm,
                                        String category) {
        try {
            UUID userId = userService.getUserDetailsFromToken(authHeader).getId();

            Pageable pagination = PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "comment_count"));
            Page<Discuss> discussPage = discussRepository.findAllWithCommentCount(searchTerm, category, pagination);
            List<DiscussDTO> discusses = discussPage.stream()
                    .peek(discuss -> log.info("Discuss ID: {}, Images: {}", discuss.getId(), discuss.getImages()))
                    .map(discuss -> discuss.toDTO(userId))
                    .collect(Collectors.toList());

            return DiscussResponse.builder()
                    .totalPage(discussPage.getTotalPages())
                    .totalElement(discussPage.getTotalElements())
                    .discussDTOs(discusses)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving discusses: ", e);
            throw new RuntimeException("Unable to retrieve discusses");
        }
    }

    @Override
    public DiscussDTO addDiscuss(AddDiscussRequest request, String authHeader) {
        try {
            User owner = userService.getUserDetailsFromToken(authHeader);
            List<Category> categories = null;
            if (request.getCategories() != null) {
                categories = categoryRepository.findAllByNames(request.getCategories().stream().map(CategoryDTO::getName).collect(Collectors.toList()));
            }
            List<Image> images = new ArrayList<>();

            List<MultipartFile> multipartFiles = request.getMultipartFiles();
            if (multipartFiles != null) {
                for (MultipartFile file : multipartFiles) {

                    String imageUrl = uploadFileService.uploadImage(file, owner.getId().toString());

                    Image image = Image.builder()
                            .imageUrl(imageUrl)
                            .build();
                    images.add(image);
                }
            }

            Discuss discuss = Discuss.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .categories(categories)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .endAt(request.getEndAt())
                    .images(images)
                    .owner(owner)
                    .build();

            for (Image image : images) {
                image.setDiscuss(discuss);
            }

            Discuss savedDiscuss = discussRepository.save(discuss);
            return savedDiscuss.toDTO(owner.getId());
        } catch (Exception e) {
            log.error("Error adding discuss: ", e);
            throw new RuntimeException("Unable to add discuss");
        }
    }

    @Override
    public DiscussDTO updateDiscuss(UUID id, UpdateDiscussRequest request) {
        try {
            Discuss discuss = discussRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Discuss not found"));

            List<Category> categories = null;
            if (request.getCategories() != null) {
                categories = categoryRepository.findAllByNames(request.getCategories().stream().map(CategoryDTO::getName).collect(Collectors.toList()));
            }

            if (request.getTitle() != null) {
                discuss.setTitle(request.getTitle());
            }
            if (request.getDescription() != null) {
                discuss.setDescription(request.getDescription());
            }
            if (categories != null) {
                discuss.setCategories(categories);
            }
            discuss.setUpdatedAt(LocalDateTime.now());
            if (request.getEndAt() != null) {
                discuss.setEndAt(request.getEndAt());
            }

            Discuss updatedDiscuss = discussRepository.save(discuss);
            return updatedDiscuss.toDTO(discuss.getOwner().getId());
        } catch (Exception e) {
            log.error("Error updating discuss: ", e);
            throw new RuntimeException("Unable to update discuss");
        }
    }

    @Override
    public void deleteDiscuss(UUID id) {
        try {
            Discuss discuss = discussRepository.findById(id)
                    .orElseThrow(()-> new IllegalArgumentException("Discuss not found"));
            discuss.setClosed(true);
            discussRepository.save(discuss);
        } catch (Exception e) {
            log.error("Error deleting discuss: ", e);
            throw new RuntimeException("Unable to delete discuss");
        }
    }

    @Override
    public DiscussDTO getDiscussById(UUID id, String authHeader) {
        Discuss discuss = discussRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Discuss not found"));
        UUID userId = userService.getUserDetailsFromToken(authHeader).getId();
        return discuss.toDTO(userId);
    }

    @Override
    public Discuss getDiscuss(UUID id) {
        return  discussRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Discuss not found"));
    }
}
