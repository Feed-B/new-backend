package com.example.team_12_be.project.comment.domain.vo;

import com.example.team_12_be.project.domain.vo.StarRank;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StarRankTest {

    @Test
    void testValidStarRank() {
        // given
        float ideaRank = 4.5f;
        float designRank = 4.0f;
        float functionRank = 5.0f;
        float completionRank = 4.5f;

        // when
        StarRank starRank = StarRank.of(ideaRank, designRank, functionRank, completionRank);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(starRank).isNotNull();
            softly.assertThat(starRank.getIdeaRank()).isEqualTo(ideaRank);
            softly.assertThat(starRank.getDesignRank()).isEqualTo(designRank);
            softly.assertThat(starRank.getFunctionRank()).isEqualTo(functionRank);
            softly.assertThat(starRank.getCompletionRank()).isEqualTo(completionRank);
        });
    }

    @Test
    void testInvalidStarRank() {
        // given
        float ideaRank = 6.0f;
        float designRank = 3.0f;
        float functionRank = 5.0f;
        float completionRank = 4.5f;

        // when / then
        assertThatThrownBy(() -> StarRank.of(ideaRank, designRank, functionRank, completionRank))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("1~5 사이의 값이어야 한다.");
    }

    @Test
    void notHalfOrWholeTest() {
        // given
        float ideaRank = 2.4f;
        float designRank = 3.0f;
        float functionRank = 5.0f;
        float completionRank = 4.5f;

        // when / then
        assertThatThrownBy(() -> StarRank.of(ideaRank, designRank, functionRank, completionRank))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0.5점 단위로 존재해야 한다.");
    }

    @Test
    void testAverageStarRank() {
        // given
        float ideaRank = 4.0f;
        float designRank = 4.0f;
        float functionRank = 5.0f;
        float completionRank = 4.5f;

        float rawAverage = (ideaRank + designRank + functionRank + completionRank) / 4;
        float roundPolicy = 10f;
        float expectedAverage = Math.round(rawAverage * roundPolicy) / roundPolicy;

        // when
        StarRank starRank = StarRank.of(ideaRank, designRank, functionRank, completionRank);

        // then
        assertThat(starRank.getAverageRank()).isEqualTo(expectedAverage);
    }
}