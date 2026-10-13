package ra.edu.hw.md04.ss04.orderservice.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import ra.edu.hw.md04.ss04.orderservice.dto.OrderRequestDTO;
import ra.edu.hw.md04.ss04.orderservice.dto.OrderResponseDTO;
import ra.edu.hw.md04.ss04.orderservice.entity.Order;
import ra.edu.hw.md04.ss04.orderservice.exception.ResourceNotFoundException;
import ra.edu.hw.md04.ss04.orderservice.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponseDTO create(OrderRequestDTO request) {
        Order order = new Order();
        order.setCustomerId(request.customerId());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(request.totalAmount());
        order.setStatus(request.status());

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toResponse(order);
    }

    public void deleteById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        orderRepository.delete(order);
    }

    private OrderResponseDTO toResponse(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }
}
