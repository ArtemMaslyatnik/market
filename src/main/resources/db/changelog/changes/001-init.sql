--liquibase formatted sql

--changeset artem:1
--comment: Full initial schema for Spring Boot 4 ERP on PostgreSQL with 15 tables and strict structural integrity

-- ==========================================
-- I. СПРАВОЧНИКИ (CATALOGS)
-- ==========================================

-- 1. COMPANIES (Организации - плоский справочник)
CREATE TABLE companies (
                           id BIGSERIAL PRIMARY KEY,
                           code VARCHAR(50) NOT NULL UNIQUE,
                           name VARCHAR(150) NOT NULL,
                           is_group BOOLEAN NOT NULL DEFAULT FALSE,
                           is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. PARTNERS (Контрагенты - иерархический справочник)
CREATE TABLE partners (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) NOT NULL UNIQUE,
                          name VARCHAR(150) NOT NULL,
                          parent_id BIGINT,
                          is_group BOOLEAN NOT NULL DEFAULT FALSE,
                          is_parent BOOLEAN NOT NULL DEFAULT FALSE,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_partner_parent FOREIGN KEY (parent_id) REFERENCES partners(id) ON DELETE RESTRICT
);
CREATE INDEX idx_partners_parent ON partners(parent_id);

-- 3. CONTRACTS (Договоры - плоский подчиненный справочник)
CREATE TABLE contracts (
                           id BIGSERIAL PRIMARY KEY,
                           code VARCHAR(50) NOT NULL UNIQUE,
                           name VARCHAR(150) NOT NULL,
                           is_group BOOLEAN NOT NULL DEFAULT FALSE,
                           is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                           contract_date TIMESTAMP NOT NULL,
                           company_id BIGINT NOT NULL,
                           partner_id BIGINT NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_contract_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT,
                           CONSTRAINT fk_contract_partner FOREIGN KEY (partner_id) REFERENCES partners(id) ON DELETE RESTRICT
);
CREATE INDEX idx_contracts_company ON contracts(company_id);
CREATE INDEX idx_contracts_partner ON contracts(partner_id);

-- 4. WAREHOUSES (Склады - иерархический справочник)
CREATE TABLE warehouses (
                            id BIGSERIAL PRIMARY KEY,
                            code VARCHAR(50) NOT NULL UNIQUE,
                            name VARCHAR(150) NOT NULL,
                            parent_id BIGINT,
                            is_group BOOLEAN NOT NULL DEFAULT FALSE,
                            is_parent BOOLEAN NOT NULL DEFAULT FALSE,
                            is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_warehouse_parent FOREIGN KEY (parent_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);
CREATE INDEX idx_warehouses_parent ON warehouses(parent_id);

-- 5. PRODUCT_CATEGORIES (Категории товаров - иерархический справочник)
CREATE TABLE product_categories (
                                    id BIGSERIAL PRIMARY KEY,
                                    code VARCHAR(50) NOT NULL UNIQUE,
                                    name VARCHAR(150) NOT NULL,
                                    parent_id BIGINT,
                                    is_group BOOLEAN NOT NULL DEFAULT FALSE,
                                    is_parent BOOLEAN NOT NULL DEFAULT FALSE,
                                    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES product_categories(id) ON DELETE RESTRICT
);
CREATE INDEX idx_categories_parent ON product_categories(parent_id);

-- 6. PRODUCTS (Номенклатура товаров - иерархический справочник)
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) NOT NULL UNIQUE,
                          name VARCHAR(150) NOT NULL,
                          parent_id BIGINT,
                          is_group BOOLEAN NOT NULL DEFAULT FALSE,
                          is_parent BOOLEAN NOT NULL DEFAULT FALSE,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_product_parent FOREIGN KEY (parent_id) REFERENCES products(id) ON DELETE RESTRICT
);
CREATE INDEX idx_products_parent ON products(parent_id);

-- 7. JOIN TABLE: PRODUCT_TO_CATEGORY (Связь Многие-ко-многим для товаров и категорий)
CREATE TABLE product_to_category (
                                     product_id BIGINT NOT NULL,
                                     category_id BIGINT NOT NULL,
                                     CONSTRAINT pk_product_to_category PRIMARY KEY (product_id, category_id),
                                     CONSTRAINT fk_p2c_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_p2c_category FOREIGN KEY (category_id) REFERENCES product_categories(id) ON DELETE CASCADE
);

-- ==========================================
-- II. ДОКУМЕНТЫ (DOCUMENTS)
-- ==========================================

-- 8. DOC_BASE (Журнал документов - центральная таблица наследования JOINED)
CREATE TABLE doc_base (
                          id BIGSERIAL PRIMARY KEY,
                          doc_number VARCHAR(50) NOT NULL UNIQUE,
                          doc_date TIMESTAMP NOT NULL,
                          is_posted BOOLEAN NOT NULL DEFAULT FALSE,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doc_base_date ON doc_base(doc_date);

-- 9. DOC_INVOICES_IN (Приходная накладная - расширяет doc_base)
CREATE TABLE doc_invoices_in (
                                 id BIGINT PRIMARY KEY,
                                 company_id BIGINT NOT NULL,
                                 partner_id BIGINT NOT NULL,
                                 contract_id BIGINT,
                                 warehouse_id BIGINT NOT NULL,
                                 total_amount NUMERIC(15,2) NOT NULL,
                                 CONSTRAINT fk_invoice_in_base FOREIGN KEY (id) REFERENCES doc_base(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_invoice_in_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT,
                                 CONSTRAINT fk_invoice_in_partner FOREIGN KEY (partner_id) REFERENCES partners(id) ON DELETE RESTRICT,
                                 CONSTRAINT fk_invoice_in_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE RESTRICT,
                                 CONSTRAINT fk_invoice_in_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);

-- 10. DOC_INVOICE_IN_ITEMS (Табличная часть Приходной накладной)
CREATE TABLE doc_invoice_in_items (
                                      id BIGSERIAL PRIMARY KEY,
                                      line_number INT NOT NULL,
                                      invoice_id BIGINT NOT NULL,
                                      product_id BIGINT NOT NULL,
                                      price NUMERIC(15,2) NOT NULL,
                                      quantity NUMERIC(20,3) NOT NULL,
                                      amount NUMERIC(15,2) NOT NULL,
                                      CONSTRAINT fk_invoice_in_item FOREIGN KEY (invoice_id) REFERENCES doc_invoices_in(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_invoice_in_prod FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);
CREATE INDEX idx_invoice_in_items_doc ON doc_invoice_in_items(invoice_id);

-- 11. DOC_INVOICES_OUT (Расходная накладная - расширяет doc_base)
CREATE TABLE doc_invoices_out (
                                  id BIGINT PRIMARY KEY,
                                  company_id BIGINT NOT NULL,
                                  partner_id BIGINT NOT NULL,
                                  contract_id BIGINT,
                                  warehouse_id BIGINT NOT NULL,
                                  total_amount NUMERIC(15,2) NOT NULL,
                                  CONSTRAINT fk_invoice_out_base FOREIGN KEY (id) REFERENCES doc_base(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_invoice_out_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT,
                                  CONSTRAINT fk_invoice_out_partner FOREIGN KEY (partner_id) REFERENCES partners(id) ON DELETE RESTRICT,
                                  CONSTRAINT fk_invoice_out_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE RESTRICT,
                                  CONSTRAINT fk_invoice_out_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);
-- 12. DOC_INVOICE_OUT_ITEMS (Табличная часть Расходной накладной)
CREATE TABLE doc_invoice_out_items (
                                       id BIGSERIAL PRIMARY KEY,
                                       line_number INT NOT NULL,
                                       invoice_id BIGINT NOT NULL,
                                       product_id BIGINT NOT NULL,
                                       price NUMERIC(15,2) NOT NULL,
                                       quantity NUMERIC(20,3) NOT NULL,
                                       amount NUMERIC(15,2) NOT NULL,
                                       CONSTRAINT fk_invoice_out_item FOREIGN KEY (invoice_id) REFERENCES doc_invoices_out(id) ON DELETE CASCADE,
                                       CONSTRAINT fk_invoice_out_prod FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);
CREATE INDEX idx_invoice_out_items_doc ON doc_invoice_out_items(invoice_id);
-- 13. DOC_MOVEMENTS (Перемещение товаров - расширяет doc_base)
CREATE TABLE doc_movements (
                               id BIGINT PRIMARY KEY,
                               company_id BIGINT NOT NULL,
                               warehouse_out_id BIGINT NOT NULL,
                               warehouse_in_id BIGINT NOT NULL,
                               CONSTRAINT fk_movement_base FOREIGN KEY (id) REFERENCES doc_base(id) ON DELETE CASCADE,
                               CONSTRAINT fk_movement_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT,
                               CONSTRAINT fk_movement_wh_out FOREIGN KEY (warehouse_out_id) REFERENCES warehouses(id) ON DELETE RESTRICT,
                               CONSTRAINT fk_movement_wh_in FOREIGN KEY (warehouse_in_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);
-- 14. DOC_MOVEMENT_ITEMS (Табличная часть Перемещения товаров)
CREATE TABLE doc_movement_items (
                                    id BIGSERIAL PRIMARY KEY,
                                    line_number INT NOT NULL,
                                    movement_id BIGINT NOT NULL,
                                    product_id BIGINT NOT NULL,
                                    quantity NUMERIC(20,3) NOT NULL,
                                    CONSTRAINT fk_movement_item_doc FOREIGN KEY (movement_id) REFERENCES doc_movements(id) ON DELETE CASCADE,
                                    CONSTRAINT fk_movement_item_prod FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);
CREATE INDEX idx_doc_movement_items_doc ON doc_movement_items(movement_id);
-- ==========================================
-- III. РЕГИСТРЫ НАКОПЛЕНИЯ (REGISTERS)
-- ==========================================
-- 15. MOVEMENT_GOODS (Регистр остатков и себестоимости «Товары на складах» для FIFO)
CREATE TABLE movement_goods (
                                id BIGSERIAL PRIMARY KEY,
                                date TIMESTAMP NOT NULL,
                                is_posted BOOLEAN NOT NULL DEFAULT TRUE,
                                movement_type VARCHAR(10) NOT NULL, -- 'INCOMING' или 'OUTGOING'
                                recorder_id BIGINT NOT NULL,       -- Ссылка на doc_base (Регистратор)
                                warehouse_id BIGINT NOT NULL,
                                product_id BIGINT NOT NULL,
                                batch_id BIGINT NOT NULL,          -- Ссылка на doc_invoices_in (Партия прихода для FIFO)
                                quantity NUMERIC(20,3) NOT NULL,   -- Общее количество (списание или приход)
                                amount NUMERIC(15,2) NOT NULL,     -- Себестоимость партии товаров
                                CONSTRAINT fk_movement_recorder FOREIGN KEY (recorder_id) REFERENCES doc_base(id) ON DELETE CASCADE,
                                CONSTRAINT fk_movement_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT,
                                CONSTRAINT fk_movement_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
                                CONSTRAINT fk_movement_batch FOREIGN KEY (batch_id) REFERENCES doc_invoices_in(id) ON DELETE RESTRICT
);
CREATE INDEX idx_movement_goods_fifo ON movement_goods (product_id, warehouse_id, date, batch_id);
CREATE INDEX idx_movement_recorder ON movement_goods (recorder_id);