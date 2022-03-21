package com.jobs.sitran.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobDescription")
public class JobDescription {

    @Id
    private String id;

    private String title;

    private String whatWeLookFor;

    private String jobRequirements;
}
