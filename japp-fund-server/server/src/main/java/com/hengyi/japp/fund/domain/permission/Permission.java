package com.hengyi.japp.fund.domain.permission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.github.ixtf.japp.core.J;
import com.google.common.collect.ImmutableSet;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.ixtf.japp.core.Constant.MAPPER;

/**
 * Created by jzb on 16-11-18.
 */
public class Permission implements Comparable<Permission>, Serializable {
    public static final Collection<String> ALL = ImmutableSet.of("*");
    public static final Function<Collection<String>, Boolean> isAllF = set -> {
        if (set.size() != 1) {
            return false;
        }
        return set.contains("*");
    };

    @NotNull
    @Size(min = 1)
    private Collection<String> corporationIds;
    @NotNull
    @Size(min = 1)
    private Collection<String> currencyIds;
    @NotNull
    @Size(min = 1)
    private Collection<RoleType> roleTypes;

    public static Collection<Permission> fromJson(String s) {
        if (J.isBlank(s)) {
            return Collections.EMPTY_SET;
        }
        JavaType type = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, Permission.class);
        try {
            List<Permission> result = MAPPER.readValue(s, type);
            Collections.sort(result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Collection<Permission> permissions) throws JsonProcessingException {
        return MAPPER.writeValueAsString(permissions);
    }

    public boolean isAllCorporation() {
        return isAllF.apply(corporationIds);
    }

    public boolean isAllCurrency() {
        return isAllF.apply(currencyIds);
    }

    public Collection<String> getCorporationIds() {
        return corporationIds;
    }

    public void setCorporationIds(Collection<String> corporationIds) {
        this.corporationIds = corporationIds;
    }

    public Collection<String> getCurrencyIds() {
        return currencyIds;
    }

    public void setCurrencyIds(Collection<String> currencyIds) {
        this.currencyIds = currencyIds;
    }

    public Collection<RoleType> getRoleTypes() {
        return roleTypes;
    }

    public void setRoleTypes(Collection<RoleType> roleTypes) {
        this.roleTypes = roleTypes;
    }

    @Override
    public int compareTo(Permission o) {
        BiFunction<Collection<String>, Collection<String>, Integer> intF = (set1, set2) -> {
            boolean b1 = isAllF.apply(set1);
            boolean b2 = isAllF.apply(set2);
            if (b1 == b2) {
                return 0;
            }
            return b1 ? -1 : 1;
        };
        int result = intF.apply(this.corporationIds, o.corporationIds);
        if (result == 0) {
            result = intF.apply(this.currencyIds, o.currencyIds);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Permission that = (Permission) o;
        return Objects.equals(corporationIds, that.corporationIds) &&
                Objects.equals(currencyIds, that.currencyIds) &&
                Objects.equals(roleTypes, that.roleTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(corporationIds, currencyIds, roleTypes);
    }

}
