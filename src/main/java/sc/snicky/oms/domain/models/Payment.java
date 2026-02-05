package sc.snicky.oms.domain.models;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Payment {
    private final String id;

    @Builder
    public Payment(String id) {
        this.id = id;
    }
}
