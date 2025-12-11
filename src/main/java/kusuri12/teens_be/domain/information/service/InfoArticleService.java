package kusuri12.teens_be.domain.information.service;

import kusuri12.teens_be.domain.information.presentation.dto.InfoArticleDto;
import kusuri12.teens_be.domain.information.domain.InfoArticle;
import kusuri12.teens_be.domain.information.domain.repository.InfoArticleRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.domain.information.exception.InfoArticleNotFoundException;
import kusuri12.teens_be.domain.user.exception.UserNotFoundException;
import kusuri12.teens_be.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfoArticleService {

    private final InfoArticleRepository infoArticleRepository;
    private final UserRepository userRepository;

    public List<InfoArticleDto.InfoArticleListResponse> getAllInfoArticles() {
        List<InfoArticle> articles = infoArticleRepository.findAllOrderByPinnedAndCreatedAt();

        return articles.stream()
                .map(article -> InfoArticleDto.InfoArticleListResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .authorName(article.getUser().getNickname())
                        .createdAt(article.getCreatedAt())
                        .pinned(article.isPinned())
                        .build())
                .collect(Collectors.toList());
    }

    public InfoArticleDto.InfoArticleDetailResponse getInfoArticleDetail(Long articleId) {
        InfoArticle article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> InfoArticleNotFoundException.EXCEPTION);

        return InfoArticleDto.InfoArticleDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorName(article.getUser().getNickname())
                .createdAt(article.getCreatedAt())
                .imageUrl(article.getImageUrl())
                .build();
    }

    @Transactional
    public void createInfoArticle(InfoArticleDto.CreateInfoArticleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        InfoArticle article = InfoArticle.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .pinned(request.isPinned())
                .imageUrl(request.getImageUrl())
                .build();

        infoArticleRepository.save(article);
    }
}