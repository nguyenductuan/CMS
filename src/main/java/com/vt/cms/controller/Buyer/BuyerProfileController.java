package com.vt.cms.controller.Buyer;

import com.vt.cms.model.dto.UpdateBuyerProfileRequest;
import com.vt.cms.model.entity.User;
import com.vt.cms.model.repository.UserRepository;
import com.vt.cms.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/buyer")
@RequiredArgsConstructor
public class BuyerProfileController {
    private final UserRepository userRepository;

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@Valid @RequestBody UpdateBuyerProfileRequest request,
                                               HttpServletRequest http) {
        Integer userId = (Integer) http.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
        User user = new User();
        user.setId(userId); user.setName(request.getName()); user.setAge(request.getAge());
        user.setPhone(request.getPhone()); user.setEmail(request.getEmail()); user.setAddress(request.getAddress());
        user.setBankName(request.getBankName()); user.setBankAccountNumber(request.getBankAccountNumber());
        user.setBankAccountHolder(request.getBankAccountHolder());
        if (userRepository.updateBuyerProfile(user) != 1) throw new IllegalStateException("Không cập nhật được hồ sơ BUYER");
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}
