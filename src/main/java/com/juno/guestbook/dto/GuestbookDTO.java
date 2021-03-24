package com.juno.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestbookDTO {

    private Long gno;

    @NotBlank(message = "제목을 입력해야해요")
    private String title;
    @NotBlank(message = "본문을 입력해야해요")
    private String content;
    @NotBlank(message = "작성자가 누구인가요?")
    private String writer;
    private LocalDateTime regDate, modDate;
}
