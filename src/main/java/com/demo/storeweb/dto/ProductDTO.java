package com.demo.storeweb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotEmpty(message = "Yêu cầu nhập tên")
    private String name;

    @NotEmpty(message = "Yêu cầu nhập hãng")
    private String brand;

    @NotEmpty(message = "Yêu cầu nhập loại")
    private String category;

    @NotEmpty(message = "Yêu cầu nhập giá")
    private String price;

    @Size(min = 10, message = "Mô tả ít nhất 10 từ")
    @Size(max = 2000, message = "Mô tả không vượt quá 2000 từ")
    private String description;

    private MultipartFile imageFile;
}
