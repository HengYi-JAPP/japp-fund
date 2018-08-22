package com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.ixtf.japp.core.J;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.hengyi.japp.fund.Constant.BALANCE_SUM_PATH;

public enum BalanceSumType {
    SS, SS_XS, SS_NB,
    FSS, FSS_JTKG, FSS_JTCG;

    private static JsonNode NODE;

    static {
        new BalanceSumTypeConfigWatchTask(BALANCE_SUM_PATH, "BalanceSumType.yml")
                .watch(node -> BalanceSumType.NODE = node);
    }

    public static JsonNode getNODE() {
        return NODE;
    }

    public static Stream<String> corporationIds() {
        return Stream.of(BalanceSumType.values())
                .map(BalanceSumType::getCorporationIds)
                .filter(Objects::nonNull)
                .flatMap(Stream::of)
                .filter(J::nonBlank);
    }

    public static Collection<BalanceSumType> getAllParent(BalanceSumType sumType) {
        final BalanceSumType parent = Optional.ofNullable(sumType)
                .map(BalanceSumType::getParent)
                .orElse(null);
        if (parent == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<BalanceSumType> result = Lists.newArrayList(parent);
        result.addAll(getAllParent(parent));
        return result;
    }

    private static Collection<BalanceSumType> getDirectSubs(BalanceSumType sumType) {
        if (sumType == null || sumType.isLeaf()) {
            return Collections.EMPTY_LIST;
        }
        return Stream.of(BalanceSumType.values())
                .parallel()
                .filter(it -> sumType == it.getParent())
                .collect(Collectors.toList());
    }

    public static Collection<BalanceSumType> getAllSub(BalanceSumType sumType) {
        if (sumType == null || sumType.isLeaf()) {
            return Collections.EMPTY_LIST;
        }
        Collection<BalanceSumType> directSubs = getDirectSubs(sumType);
        if (J.isEmpty(directSubs)) {
            return Collections.EMPTY_LIST;
        }
        Collection<BalanceSumType> result = Lists.newArrayList(directSubs);
        Collection<BalanceSumType> subs = directSubs.stream()
                .parallel()
                .map(BalanceSumType::getAllSub)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        result.addAll(subs);
        return result;
    }

    public String getDisplayName() {
        return Optional.ofNullable(NODE.get(this.name()))
                .map(it -> it.get("displayName"))
                .map(it -> it.asText(null))
                .orElse(this.name());
    }

    public BalanceSumType getParent() {
        return Optional.ofNullable(NODE.get(this.name()))
                .map(it -> it.get("parent"))
                .map(it -> it.asText(null))
                .map(BalanceSumType::valueOf)
                .orElse(null);
    }

    public String[] getCorporationIds() {
        return Optional.ofNullable(NODE.get(this.name()))
                .map(it -> it.get("corporationIds"))
                .map(ids -> StreamSupport.stream(ids.spliterator(), false)
                        .map(it -> it.asText(null))
                        .filter(J::nonBlank)
                        .toArray(String[]::new)
                )
                .orElse(new String[0]);
    }

    private boolean isLeaf() {
        return getCorporationIds().length > 0;
    }

}
