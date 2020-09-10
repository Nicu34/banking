package com.ing.banking.model;

import com.ing.banking.util.Constants;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class BankAccountRequest {

    @NotNull(message = "User id cannot be empty.")
    private int userId;

    @Pattern(regexp = "(SAVINGS|OTHER)", message = "Invalid account type. It could be SAVINGS or OTHER.")
    private String accountType;

    @NotNull(message = "Bank branch id cannot be empty.")
    private int bankBranchId;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} [+-]\\d{2}$", message = "Invalid date time format. Format should be yyyy-MM-dd HH:mm:ss x. Example: 2020-09-10 17:00:00 +02")
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private String creationDateTime;
}
