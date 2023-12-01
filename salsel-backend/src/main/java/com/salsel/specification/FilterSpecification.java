package com.salsel.specification;

import com.salsel.dto.projectEnums.Mapper;
import javax.persistence.criteria.Expression;
import com.salsel.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FilterSpecification<T>{
    public Specification<T> getSearchSpecification(Class<T> entityClass, SearchCriteria searchCriteria) {

        searchCriteria.setSearchText(searchCriteria.getSearchText()
                .replace("\\", "\\\\")
                .replace("%2B","\\+")
                .replace("%", "\\%")
                .replace("_", "\\_")
                .trim());

        String[] models = this.getEntityFields(entityClass);

            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                for (String fieldName : models) {
                    if(!fieldName.equals("id")){
                        if (root.get(fieldName).getJavaType().equals(String.class)) {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldName)),
                                    "%" + searchCriteria.getSearchText().toLowerCase() + "%"));
                        } else if (root.get(fieldName).getJavaType() == LocalDate.class) {
                            try {
                                LocalDate searchDate = LocalDate.parse(searchCriteria.getSearchText());
                                predicates.add(criteriaBuilder.equal(root.get(fieldName), searchDate));
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid LocalDate format: " + searchCriteria.getSearchText());
                            }
                        } else if (root.get(fieldName).getJavaType() == LocalTime.class) {
                            try {
                                LocalTime searchTime = LocalTime.parse(searchCriteria.getSearchText());
                                predicates.add(criteriaBuilder.equal(root.get(fieldName), searchTime));
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid LocalTime format: " + searchCriteria.getSearchText());
                            }
                        }else if (root.get(fieldName).getJavaType() == LocalDateTime.class) {
                            try {
                                LocalDateTime searchDateTime = LocalDateTime.parse(searchCriteria.getSearchText());
                                predicates.add(criteriaBuilder.equal(root.get(fieldName), searchDateTime));
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid LocalDateTime format: " + searchCriteria.getSearchText());
                            }
                        }else if (root.get(fieldName).getJavaType() == Long.class) {
                            try {
                                Long searchValue = Long.parseLong(searchCriteria.getSearchText());
                                predicates.add(criteriaBuilder.equal(root.get(fieldName), searchValue));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid Long format: " + searchCriteria.getSearchText());
                            }
                        }else if (root.get(fieldName).getJavaType() == Boolean.class) {
                            try {
                                Boolean searchValue = Boolean.parseBoolean(searchCriteria.getSearchText());
                                predicates.add(criteriaBuilder.equal(root.get(fieldName), searchValue));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid Boolean format: " + searchCriteria.getSearchText());
                            }
                        }
                    }
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[2]));
            };
    }

    String[] getEntityFields(Class<T> entityClass){
        Field[] fields =  entityClass.getDeclaredFields();
        return Arrays.stream(fields)
                .map(Field::getName)
                .toArray(String[]::new);
    }

}
