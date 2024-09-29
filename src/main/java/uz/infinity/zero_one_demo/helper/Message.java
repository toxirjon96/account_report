package uz.infinity.zero_one_demo.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String messageUz;
    private String messageRu;
    private String messageEn;
}
