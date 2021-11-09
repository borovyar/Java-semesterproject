package com.tjv.semesterproject.services;

import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.entity.ProductEntity;
import com.tjv.semesterproject.exceptions.OrderNotExistException;
import com.tjv.semesterproject.exceptions.ProductExistException;
import com.tjv.semesterproject.exceptions.ProductNotExistException;
import com.tjv.semesterproject.model.ProductAllOrdersDto;
import com.tjv.semesterproject.model.ProductDto;
import com.tjv.semesterproject.model.ProductOrderDto;
import com.tjv.semesterproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void registerProduct(ProductEntity entity) throws ProductExistException {
        if (productRepository.findById(entity.getName()).isPresent())
            throw new ProductExistException("Product Already Exist");
        productRepository.save(entity);
    }

    public void updateProduct(ProductDto productDto, String id) throws ProductNotExistException {
        if (productRepository.findById(id).isEmpty())
            throw new ProductNotExistException("Product doesn't Exist");
        ProductEntity product = productRepository.findById(id).get();
        product.setPrice(productDto.getPrice());
        productRepository.save(product);
    }

    public ProductDto getProduct(String id) throws ProductNotExistException {
        if (productRepository.findById(id).isEmpty())
            throw new ProductNotExistException("Product does not exist!");
        return ProductDto.toModel(productRepository.findById(id).get());
    }

    public ProductOrderDto getProductOrder(String product_id, Integer order_id) throws ProductNotExistException, OrderNotExistException {
        if (productRepository.findById(product_id).isEmpty())
            throw new ProductNotExistException("Product does not exist!");
        ProductEntity product = productRepository.findById(product_id).get();
        if (product.getOrders().size() < order_id)
            throw new OrderNotExistException("Order does not exist");
        return ProductOrderDto.toOrderModel(product, product.getOrders().get(order_id));
    }

    public void deleteProduct(String id) throws ProductNotExistException {
        if( productRepository.findById(id).isEmpty())
            throw new ProductNotExistException("product does not exist");
        for(OrderEntity order : productRepository.findById(id).get().getOrders())
            order.removeProduct(productRepository.findById(id).get());
        productRepository.deleteById(id);
    }

    public Collection<ProductDto> readAll() {
        Set<ProductDto> productsDto = new HashSet<>();
        for (ProductEntity product : productRepository.findAll())
            productsDto.add(ProductDto.toModel(product));
        return productsDto;
    }

    public ProductAllOrdersDto readAllOrders(String product_id) throws ProductNotExistException {
        if (productRepository.findById(product_id).isEmpty())
            throw new ProductNotExistException("product does not exist!");
        return ProductAllOrdersDto.toModel(productRepository.findById(product_id).get());
    }
}
