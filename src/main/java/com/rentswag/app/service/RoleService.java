package com.rentswag.app.service;

import com.rentswag.app.model.Role;

public interface RoleService {
    Role findByName(String name);
}
