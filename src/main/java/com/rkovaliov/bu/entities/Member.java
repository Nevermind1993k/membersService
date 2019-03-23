package com.rkovaliov.bu.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "members")
public class Member {

    @Id
    private ObjectId _id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    @NotNull
    private String postalCode;

    private Binary image;

    public Member() {
    }

    public Member(@NotNull String firstName, @NotNull String lastName, @NotNull Date dateOfBirth, @NotNull String postalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
    }

    public String get_id() {
        return _id.toHexString();
    }
}
