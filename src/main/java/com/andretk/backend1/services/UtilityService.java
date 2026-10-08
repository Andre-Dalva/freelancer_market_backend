package com.andretk.backend1.services;

import com.andretk.backend1.enums.UserRole;
import com.andretk.backend1.exceptions.ForbiddenException;
import com.andretk.backend1.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    public Authentication getAuthentication(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UnauthorizedException("User not authenticated");

        return  authentication;
    }

    public Long getCurrentUserId(){
        Long id = (Long) getAuthentication().getPrincipal();
        if (id == null)
            throw new UnauthorizedException("User not authenticated");

        return id;
    }

    public boolean isOwner (Long id){
        if (id == null) return false;

        return id.equals(getCurrentUserId());
    }

    public boolean matchesThisRole(UserRole role){
        return getAuthentication().getAuthorities().stream()
                .anyMatch(a ->
                a.getAuthority().equals("ROLE_" + role.name()));

    }

    public boolean isOwnerOrAdmin(Long userId){
        boolean isOwner = isOwner(userId);

        boolean isAdmin = matchesThisRole(UserRole.ADMIN);

        return isOwner || isAdmin;
    }

    public void validateOwnerOrAdmin(Long userId) {
        if (!isOwnerOrAdmin(userId)) {
            throw new ForbiddenException("Current user does not have permission to execute this action");
        }
    }
}
