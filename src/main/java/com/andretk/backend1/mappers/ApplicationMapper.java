package com.andretk.backend1.mappers;

import com.andretk.backend1.dtos.ApplicationDto;
import com.andretk.backend1.entities.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(source = "job.recruiter.name", target = "job.recruiterName")
    ApplicationDto toDto(Application application);
}
