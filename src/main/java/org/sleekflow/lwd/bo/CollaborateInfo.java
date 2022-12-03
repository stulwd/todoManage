package org.sleekflow.lwd.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class CollaborateInfo implements Serializable {

    public Long creater;
    public Set<Long> members;
    public Set<Long> sharedIds;
    public String password;

    public CollaborateInfo() {
    }

    public CollaborateInfo(Long createrId, Set<Long> members, Set<Long> sharedIds, String password) {
        this.creater = createrId;
        this.members = members;
        this.sharedIds = sharedIds;
        this.password = password;
    }


}
