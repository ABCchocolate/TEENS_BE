package kusuri12.teens_be.domain.information.service;

import kusuri12.teens_be.domain.information.presentation.dto.request.CreateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.request.UpdateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationDetailResponse;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationListResponse;
import kusuri12.teens_be.domain.information.domain.Information;
import kusuri12.teens_be.domain.information.domain.repository.InformationRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.domain.information.exception.InformationNotFoundException;
import kusuri12.teens_be.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository infoArticleRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<InformationListResponse> getAllInfoArticles() {
        List<Information> articles = infoArticleRepository.findAllOrderByPinnedAndCreatedAt();

        return articles.stream()
                .map(article -> InformationListResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .authorName(article.getUser().getNickname())
                        .createdAt(article.getCreatedAt())
                        .pinned(article.isPinned())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<InformationListResponse> searchInfoArticles(String keyword) {
        List<Information> articles = infoArticleRepository.searchByKeyword(keyword);

        return articles.stream()
                .map(article -> InformationListResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .authorName(article.getUser().getNickname())
                        .createdAt(article.getCreatedAt())
                        .pinned(article.isPinned())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public InformationDetailResponse getInfoArticleDetail(Long articleId) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> InformationNotFoundException.EXCEPTION);

        return InformationDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorName(article.getUser().getNickname())
                .createdAt(article.getCreatedAt())
                .imageUrl(article.getImageUrl())
                .build();
    }

    @Transactional
    public void createInfoArticle(Long userId, CreateInformationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Information article = Information.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .pinned(request.isPinned())
                .imageUrl(request.getImageUrl())
                .build();

        infoArticleRepository.save(article);
    }

    @Transactional
    public void updateInfoArticle(Long articleId, UpdateInformationRequest request) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> InformationNotFoundException.EXCEPTION);

        article.updateTitleAndContent(request.getTitle(), request.getContent(),
                request.isPinned(), request.getImageUrl());
    }

    @Transactional
    public void deleteInfoArticle(Long articleId) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> InformationNotFoundException.EXCEPTION);

        infoArticleRepository.delete(article);
    }
}