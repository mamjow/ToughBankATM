package model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private  String jwtToken;
    private Date exprireDate;
    private ClientInformationViewModel owner;
    private String iban;
}
