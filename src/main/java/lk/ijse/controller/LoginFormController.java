package lk.ijse.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lk.ijse.bo.UserBO;
import lk.ijse.bo.UserBOImpl;
import lk.ijse.model.UserDTO;
import lk.ijse.service.CrudUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    public Label lblLogin;
    public Label lblCreateAcc;
    public JFXTextArea lblDescription;
    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton btn;

    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXTextField textPassword;

    public static String userName;
    public static String password;


    private UserBO userBO = new UserBOImpl();

    @FXML
    void btnGoToChatOnAction(ActionEvent event) {
        userName = txtUserName.getText();
        password = textPassword.getText();
        if (userName.isBlank() || password.isBlank()){
            new Alert(Alert.AlertType.WARNING, "Fiels are empty!").show();
            return;
        }

        if (btn.getText().equals("Create Account")){
            try {
                boolean isUserSaved = saveUser(userName, password);
                if (!isUserSaved){
                    new Alert(Alert.AlertType.WARNING, "User didn't saved").show();
                }
                btn.setText("Go To Chat");
                lblDescription.setVisible(false);
                lblLogin.setVisible(true);
                new Alert(Alert.AlertType.CONFIRMATION, "Account created succesfuly.Please enter details again to login").show();
                return;
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Oops..Something went wrong").show();
                e.printStackTrace();
            }
        }



        try {
            Boolean isUserValid = checkUser(userName, password);
            if (!isUserValid){
                new Alert(Alert.AlertType.WARNING, "User Details are incorrect!!").show();
                return;
            }
        } catch (SQLException e) {
            System.out.println("user checking error!!!");
            e.printStackTrace();
        }

        try {

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(new FXMLLoader().load(getClass().getResource("/view/chat_form.fxml"))));
            stage.setTitle(userName + "'s Chat");

        } catch (IOException e) {
            e.printStackTrace();
        }

        userName = null;

    }

    @FXML
    void lblCreateAccOnMoueClicked(MouseEvent event) {
        lblLogin.setVisible(false);
        lblDescription.setVisible(true);

        System.out.println("Create a account");
        btn.setText("Create Account");
    }

    private Boolean checkUser(String userName, String password) throws SQLException {
        return userBO.checkUser(userName, password);
    }

    private boolean saveUser(String userName, String password) throws SQLException {
        return userBO.saveUser(new UserDTO(CrudUtil.getNewId(userBO.getlastId()), userName, password));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUserName.setOnAction((e) -> {
            textPassword.requestFocus();
        });

        textPassword.setOnAction((e) -> {
            btn.fire();
        });
    }
}
