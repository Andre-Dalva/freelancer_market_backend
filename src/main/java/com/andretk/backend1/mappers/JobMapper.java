package com.andretk.backend1.mappers;

import com.andretk.backend1.dtos.CreateJobRequest;
import com.andretk.backend1.dtos.JobDto;
import com.andretk.backend1.entities.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(source = "recruiter.name", target = "recruiterName")
    JobDto toJobDto(Job job);
    Job toJob(CreateJobRequest request);
    void update(CreateJobRequest request, @MappingTarget Job job);
}
