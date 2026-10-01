--liquibase formatted sql

--changeset artem:1
--comment: Final initial schema for Spring Boot 4 ERP on PostgreSQL with BIGINT keys

-- 1. COMPANIES
CREATE TABLE companies (
                           id BIGSERIAL PRIMARY KEY,
                           code VARCHAR(50) NOT NULL UNIQUE,
                           name VARCHAR(150) NOT NULL,
                           is_group BOOLEAN NOT NULL DEFAULT FALSE,
                           is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP NOT NULL
);

-- 2. PARTNERS
CREATE TABLE partners (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) NOT NULL UNIQUE,
                          name VARCHAR(150) NOT NULL,
                          parent_id BIGINT, -- ДОБАВИЛИ ЭТУ СТРОКУ
                          is_group BOOLEAN NOT NULL DEFAULT FALSE,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          CONSTRAINT fk_partner_parent FOREIGN KEY (parent_id) REFERENCES partners(id) ON DELETE SET NULL -- И ВНЕШНИЙ КЛЮЧ
);

-- 3. CONTRACTS
CREATE TABLE contracts (
                           id BIGSERIAL PRIMARY KEY,
                           code VARCHAR(50) NOT NULL UNIQUE,
                           name VARCHAR(150) NOT NULL,
                           is_group BOOLEAN NOT NULL DEFAULT FALSE,
                           is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                           contract_date TIMESTAMP NOT NULL,
                           company_id BIGINT NOT NULL,
                           partner_id BIGINT NOT NULL,
                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP NOT NULL,
                           CONSTRAINT fk_contract_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT,
                           CONSTRAINT fk_contract_partner FOREIGN KEY (partner_id) REFERENCES partners(id) ON DELETE RESTRICT
);

-- 4. PRODUCTS
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) NOT NULL UNIQUE,
                          name VARCHAR(150) NOT NULL,
                          parent_id BIGINT,
                          is_group BOOLEAN NOT NULL DEFAULT FALSE,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          CONSTRAINT fk_product_parent FOREIGN KEY (parent_id) REFERENCES products(id) ON DELETE SET NULL
);

-- 5. WAREHOUSES
CREATE TABLE warehouses (
                            id BIGSERIAL PRIMARY KEY,
                            code VARCHAR(50) NOT NULL UNIQUE,
                            name VARCHAR(150) NOT NULL,
                            parent_id BIGINT,
                            is_group BOOLEAN NOT NULL DEFAULT FALSE,
                            is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                            created_at TIMESTAMP NOT NULL,
                            updated_at TIMESTAMP NOT NULL,
                            CONSTRAINT fk_warehouse_parent FOREIGN KEY (parent_id) REFERENCES warehouses(id) ON DELETE SET NULL
);

-- 6. PRODUCT_CATEGORIES
CREATE TABLE product_categories (
                                    id BIGSERIAL PRIMARY KEY,
                                    code VARCHAR(50) NOT NULL UNIQUE,
                                    name VARCHAR(150) NOT NULL,
                                    parent_id BIGINT,
                                    is_group BOOLEAN NOT NULL DEFAULT FALSE,
                                    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                                    created_at TIMESTAMP NOT NULL,
                                    updated_at TIMESTAMP NOT NULL,
                                    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES product_categories(id) ON DELETE SET NULL
);

-- 7. JOIN TABLE: PRODUCT_TO_CATEGORY
CREATE TABLE product_to_category (
                                     product_id BIGINT NOT NULL,
                                     category_id BIGINT NOT NULL,
                                     CONSTRAINT pk_product_to_category PRIMARY KEY (product_id, category_id),
                                     CONSTRAINT fk_p2c_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_p2c_category FOREIGN KEY (category_id) REFERENCES product_categories(id) ON DELETE CASCADE
);

-- 8. DOC_BASE (Центральная таблица наследования документов)
CREATE TABLE doc_base (
                          id BIGSERIAL PRIMARY KEY,
                          doc_number VARCHAR(50) NOT NULL UNIQUE,
                          is_posted BOOLEAN NOT NULL DEFAULT FALSE,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL
);

-- 9. DOC_INVOICES_IN (Приходы - расширяет doc_base)
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

-- 10. DOC_INVOICE_IN_ITEMS (Строки прихода)
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

-- 11. DOC_INVOICES_OUT (Продажи - расширяет doc_base)
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

-- 12. DOC_INVOICE_OUT_ITEMS (Строки продаж)
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

-- 13. DOC_STOCK_TRANSFERS (Перемещения - расширяет doc_base)
CREATE TABLE doc_stock_transfers (
                                     id BIGINT PRIMARY KEY,
                                     company_id BIGINT NOT NULL,
                                     source_warehouse_id BIGINT NOT NULL,
                                     destination_warehouse_id BIGINT NOT NULL,
                                     CONSTRAINT fk_transfer_base FOREIGN KEY (id) REFERENCES doc_base(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_transfer_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT,
                                     CONSTRAINT fk_transfer_source FOREIGN KEY (source_warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT,
                                     CONSTRAINT fk_transfer_dest FOREIGN KEY (destination_warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);

-- 14. DOC_STOCK_TRANSFER_ITEMS (Строки перемещений)
CREATE TABLE doc_stock_transfer_items (
                                          id BIGSERIAL PRIMARY KEY,
                                          line_number INT NOT NULL,
                                          transfer_id BIGINT NOT NULL,
                                          product_id BIGINT NOT NULL,
                                          quantity NUMERIC(20,3) NOT NULL,
                                          CONSTRAINT fk_transfer_item FOREIGN KEY (transfer_id) REFERENCES doc_stock_transfers(id) ON DELETE CASCADE,
                                          CONSTRAINT fk_transfer_prod FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

-- 15. MOVEMENT_GOODS (Плоский транзакционный регистр остатков товаров)
CREATE TABLE movement_goods (
                                id BIGSERIAL PRIMARY KEY,
                                is_active BOOLEAN NOT NULL DEFAULT TRUE,
                                movement_type VARCHAR(20) NOT NULL,
                                company_id BIGINT NOT NULL,
                                document_id BIGINT NOT NULL,
                                line_number INT,
                                product_id BIGINT NOT NULL,
                                warehouse_id BIGINT NOT NULL,
                                batch_id BIGINT NOT NULL,
                                quantity NUMERIC(20,3) NOT NULL,
                                amount NUMERIC(15,2) NOT NULL,
                                movement_date TIMESTAMP NOT NULL,
                                CONSTRAINT fk_mov_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT,
                                CONSTRAINT fk_mov_doc_base FOREIGN KEY (document_id) REFERENCES doc_base(id) ON DELETE CASCADE,
                                CONSTRAINT fk_mov_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
                                CONSTRAINT fk_mov_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT,
                                CONSTRAINT fk_mov_batch FOREIGN KEY (batch_id) REFERENCES doc_invoices_in(id) ON DELETE RESTRICT
);
