package com.waleed.dreamshops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.waleed.dreamshops.dto.OrderDto;
import com.waleed.dreamshops.enums.OrderStatus;
import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Cart;
import com.waleed.dreamshops.model.Order;
import com.waleed.dreamshops.model.OrderItem;
import com.waleed.dreamshops.model.Product;
import com.waleed.dreamshops.repository.OrderRepository;
import com.waleed.dreamshops.repository.ProductRepository;
import com.waleed.dreamshops.service.cart.ICartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ICartService cartService;
	private final ModelMapper modelMapper;
	
	@Transactional
	@Override
	public Order placeOrder(Long userId) {
		Cart cart = cartService.getCartByUserId(userId); 
		
		Order order = createOrder(cart);
		List<OrderItem> orderItemList = createOrderItems(order, cart);
		
		order.setOrderItems(new HashSet<>(orderItemList));
		order.setTotalAmount(calculateTotalAmount(orderItemList));
		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(cart.getId());
		
		return savedOrder;
	}
	
	private Order createOrder(Cart cart) {
		Order order = new Order();
		order.setUser(cart.getUser()); 
		order.setOrderStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDate.now());
		return order;
	}
	
	private List<OrderItem> createOrderItems(Order order, Cart cart) {
		return cart.getItems().stream().map(cartItem -> {
			Product product = cartItem.getProduct();
			product.setInventory(product.getInventory() - cartItem.getQuantity());
			productRepository.save(product);
			return new OrderItem(
				order,
				product,
				cartItem.getQuantity(),
				cartItem.getUnitPrice()
			);
		}).toList();
				
	}
	private BigDecimal calculateTotalAmount(List<OrderItem> orderIteList) {
		return orderIteList.stream()
				.map(item -> item.getPrice()
						.multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal :: add);	
	}

	@Override
	public OrderDto getOrder(Long orderId) {
		return orderRepository.findById(orderId)
				.map(this :: convertToDto)
				.orElseThrow(()->
					new ResourceNotFoundException("Order not found. id:"+orderId));
	}
	@Override
	public List<OrderDto> getUserOrders(Long userId) {
		return orderRepository.findByUserId(userId).stream()
				.map(this :: convertToDto).toList();
	}
	@Override
	public OrderDto convertToDto(Order order) {
		return modelMapper.map(order, OrderDto.class);
	}

}
