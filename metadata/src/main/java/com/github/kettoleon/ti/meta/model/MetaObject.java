package com.github.kettoleon.ti.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.kettoleon.ti.meta.MetaRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MetaObject {

    private String dataName;
    private String friendlyName;

    private MetaRepository metaRepository;

    protected <T extends MetaObject> Optional<T> getReference(String templateName, Class<T> cls) {
        return metaRepository.get(templateName, cls);
    }

    protected <T extends MetaObject> List<T> getReferenceArray(List<String> templateName, Class<T> cls) {
        return templateName.stream().filter(StringUtils::isNotBlank).flatMap(tn -> metaRepository.get(tn, cls).stream()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaObject that = (MetaObject) o;
        return Objects.equals(dataName, that.dataName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataName);
    }
}
