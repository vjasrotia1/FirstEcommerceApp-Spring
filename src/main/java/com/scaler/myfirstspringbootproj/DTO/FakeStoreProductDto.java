package com.scaler.myfirstspringbootproj.DTO;


import com.scaler.myfirstspringbootproj.models.Category;
import com.scaler.myfirstspringbootproj.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDto {
//these fields should be similar to the names of the  "fields" of fakestoreapi.com
    private Long id;
    private String title;
    private String category;
    private Double price;
    private String description;
    private String image;


    //this will convert the fakestore product to product of  my implementation
    public Product getProduct() {
Product p=new  Product();
p.setId(this.id);
p.setTitle(this.title);
p.setDescription(this.description);
p.setPrice(this.price);
p.setImageUrl(this.image);

Category c=new  Category();
c.setName(this.category);
p.setCategory(c);

return p;
    }

    @Override
    public String toString() {
        return "FakeStoreProductDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
