package mft.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private List<SimCard> simCardList;

    public void addSimCard(SimCard simCard) throws Exception {
        if (simCardList == null) {
            simCardList = new ArrayList<>();
        }

        // Business Logic:
        if (simCardList.size() < 11) {
            simCard.setOwner(this);
            simCardList.add(simCard);
        } else {
            throw new Exception("خطا: شما بیشتر از 10 عدد سیم کارت نمی توانید داشته باشید!");
        }
    }
}
