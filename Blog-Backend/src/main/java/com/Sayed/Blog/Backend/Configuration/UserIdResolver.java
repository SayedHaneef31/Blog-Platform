// package com.Sayed.Blog.Backend.Configuration;
// 
// import com.Sayed.Blog.Backend.Entity.User;
// import com.Sayed.Blog.Backend.Repository.UserRepo;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.MethodParameter;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.annotation.RequestAttribute;
// import org.springframework.web.bind.support.WebDataBinderFactory;
// import org.springframework.web.context.request.NativeWebRequest;
// import org.springframework.web.method.support.HandlerMethodArgumentResolver;
// import org.springframework.web.method.support.ModelAndViewContainer;
// 
// import java.util.UUID;
// 
// @Component
// public class UserIdResolver implements HandlerMethodArgumentResolver {
// 
//     @Autowired
//     private UserRepo userRepo;
// 
//     @Override
//     public boolean supportsParameter(MethodParameter parameter) {
//         return parameter.hasParameterAnnotation(RequestAttribute.class) &&
//                parameter.getParameterType().equals(UUID.class) &&
//                parameter.getParameterName().equals("userId");
//     }
// 
//     @Override
//     public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//                                 NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//         
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         
//         if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//             UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//             String email = userDetails.getUsername();
//             
//             User user = userRepo.findByEmail(email).orElse(null);
//             if (user != null) {
//                 return user.getId();
//             }
//         }
//         
//         return null;
//     }
// } 