package com.andretk.backend1.mappers;

import com.andretk.backend1.dtos.AcceptJobRequest;
import com.andretk.backend1.dtos.AcceptedJobDto;
import com.andretk.backend1.entities.AcceptedJobs;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface AcceptedJobsMapper {
    AcceptedJobDto toDto(AcceptedJobs acceptedJobs);
    void update(AcceptJobRequest request, @MappingTarget AcceptedJobs acceptedJobs);
}
