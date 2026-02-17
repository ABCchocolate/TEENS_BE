package kusuri12.teens_be.domain.information.service;

import kusuri12.teens_be.domain.information.exception.InfoErrorCode;
import kusuri12.teens_be.domain.information.presentation.dto.request.CreateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.request.UpdateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationDetailResponse;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationListResponse;
import kusuri12.teens_be.domain.information.domain.Information;
import kusuri12.teens_be.domain.information.repository.InformationRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.security.annotation.CheckAuthor;
import kusuri12.teens_be.global.security.annotation.CheckId;
import kusuri12.teens_be.global.security.aspect.Authorizable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InformationService implements Authorizable {

    private final InformationRepository infoArticleRepository;
    private final UserRepository userRepository;

    // todo: 페이징 처리하기
    // 게시글 전체 조회
    public List<InformationListResponse> getAllInfoArticles() {
        List<Information> articles = infoArticleRepository.findAllByOrderByCreatedAtDesc();

        return articles.stream()
                .map(article -> InformationListResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .authorName(article.getUser().getNickname())
                        .createdAt(article.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 검색
    public List<InformationListResponse> searchInfoArticles(String keyword) {
        List<Information> articles = infoArticleRepository.searchByKeyword(keyword);

        return articles.stream()
                .map(article -> InformationListResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .authorName(article.getUser().getNickname())
                        .createdAt(article.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public InformationDetailResponse getInfoArticleDetail(Long articleId) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));

        return InformationDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorName(article.getUser().getNickname())
                .createdAt(article.getCreatedAt())
                .build();
    }

    // 게시글 생성
    @Transactional
    public void createInfoArticle(Long userId, CreateInformationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        Information article = Information.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();

        infoArticleRepository.save(article);
    }

    @Transactional
    @CheckAuthor
    public void updateInfoArticle(@CheckId Long articleId, UpdateInformationRequest request) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));

        article.updateTitleAndContent(request.title(), request.content());
    }

    @Transactional
    @CheckAuthor
    public void deleteInfoArticle(@CheckId Long articleId) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));

        infoArticleRepository.delete(article);
    }

    @Override
    public Long getAuthorId(Long resourceId) {
        return infoArticleRepository.findById(resourceId)
                .map(article -> article.getUser().getId())
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));
    }
}