package cm.sji.hotel_reservation.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${hotelphoto.upload.dir}")
    private String hotelphotoDir;

    @Value("${roomphoto.upload.dir}")
    private String roomphotoDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/" + hotelphotoDir + "/**",
                        "/" + roomphotoDir + "/**")
                .addResourceLocations("file:" + hotelphotoDir + "/")
                .addResourceLocations("file:" + roomphotoDir + "/");
    }
}
