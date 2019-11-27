package com.dongho.dev.infrasturcture.persistence.jpa.item;

import com.dongho.dev.domain.item.Item;
import com.dongho.dev.domain.item.ItemRepositoryCutom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ItemRepositoryImpl extends QuerydslRepositorySupport implements ItemRepositoryCutom {

    public ItemRepositoryImpl() {
        super(Item.class);
    }

}
