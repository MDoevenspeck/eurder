package com.switchfully.eurder.repositories;

import com.switchfully.eurder.model.orders.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemGroupRepository extends JpaRepository <ItemGroup, Long>{
}
