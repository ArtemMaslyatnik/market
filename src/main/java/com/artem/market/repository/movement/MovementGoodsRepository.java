package com.artem.market.repository.movement;

import com.artem.market.model.movement.MovementGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementGoodsRepository extends JpaRepository<MovementGoods, Long> {
    // Сюда мы позже добавим кастомные методы для подсчета остатков на складах
}