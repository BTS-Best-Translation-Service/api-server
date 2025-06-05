package project.bts.common.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class SrtDto {

    @Getter
    @Setter
    public static class SrtRequest {
        private UUID userId;
        private String videoTitle;
        private String videoUrl;
    }

    @Getter
    @Setter
    public static class SrtLinkRequest {
        private String videoTitle;
        private String videoUrl;
    }
}
