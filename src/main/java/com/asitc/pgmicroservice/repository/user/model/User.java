package com.asitc.pgmicroservice.repository.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Table(name = "user", schema = "public")
@Data
@ApiModel("User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(name = "Id", notes = "Auto generated identifier", dataType = "Long", example = "1")
    private Long id;
    @NotNull
    @Length(max = 150)
    @ApiModelProperty(name = "First name", notes = "User's first name, limited to 150 characters", dataType = "String", example = "John", required = true)
    private String firstName;
    @NotNull
    @Length(max = 250)
    @ApiModelProperty(name = "Email", notes = "User's email address, limited to 250 characters", dataType = "String", example = "john.smith@email.co.uk", required = true)
    private String email;

}
