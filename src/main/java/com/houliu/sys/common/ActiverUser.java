package com.houliu.sys.common;

import com.houliu.sys.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author houliu
 * @create 2019-12-30 12:30
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser {

    private User user;
    private List<String> roles;
    private List<String> permissions;

}
