package com.scaler.myfirstspringbootproj.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
public class LoginHistory extends BaseModel {

        @ManyToOne
        private User user;

        private String ip;

        private String device;

        private Date loginTime;
    }

