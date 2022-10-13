package com.sigma.clotheswarehouse.entity;

import com.sigma.clotheswarehouse.entity.template.AbsUUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OutcomeMaterial extends AbsUUID {

    @ManyToMany(cascade = CascadeType.ALL)
    private List<ResourceForOutcomeMaterial> resources;

    @ManyToOne
    private Product product;

    private double productAmount;

    private double productNewPrice;

    private double productOldPrice;
}
