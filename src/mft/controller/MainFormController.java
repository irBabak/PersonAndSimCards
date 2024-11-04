package mft.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mft.model.entity.Person;
import mft.model.entity.SimCard;
import mft.model.service.PersonService;
import mft.model.service.SimCardService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    @FXML
    private Button clearFormBtn, saveBtn, editBtn, removeBtn;

    @FXML
    private TextField idTxt, firstNameTxt, lastNameTxt, nationalCodeTxt, simcardNumberTxt;

    @FXML
    private Label countTxt;

    @FXML
    private TableView<Person> personsTbl;

    @FXML
    private TableColumn<Person, Integer> personIdCol;

    @FXML
    private TableColumn<Person, String> firstNameCol, lastNameCol, nationalCodeCol;

    @FXML
    private TableView<SimCard> simcardsTbl;

    @FXML
    private TableColumn<SimCard, Integer> simcardIdCol;

    @FXML
    private TableColumn<SimCard, String> simcardNumberCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();

        clearFormBtn.setOnAction(event -> resetForm());

        saveBtn.setOnAction(event -> {
            try {
                Person person =
                        Person
                                .builder()
                                .firstName(firstNameTxt.getText())
                                .lastName(lastNameTxt.getText())
                                .nationalCode(nationalCodeTxt.getText())
                                .build();

                SimCard simCard =
                        SimCard
                                .builder()
                                .simCardNumber(simcardNumberTxt.getText())
                                .owner(person)
                                .build();

                PersonService.getPersonService().save(person);
                SimCardService.getSimCardService().save(simCard);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Saved Successfully", ButtonType.OK);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Saving Failed: " + e.getMessage(), ButtonType.OK);
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try {
                Person person =
                        Person
                                .builder()
                                .id(Integer.parseInt(idTxt.getText()))
                                .firstName(firstNameTxt.getText())
                                .lastName(lastNameTxt.getText())
                                .nationalCode(nationalCodeTxt.getText())
                                .build();

                SimCard simCard =
                        SimCard
                                .builder()
                                .id(Integer.parseInt(idTxt.getText()))
                                .simCardNumber(simcardNumberTxt.getText())
                                .owner(person)
                                .build();

                PersonService.getPersonService().edit(person);
                SimCardService.getSimCardService().edit(simCard);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Edited Successfully", ButtonType.OK);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Editing Failed: " + e.getMessage(), ButtonType.OK);
                alert.show();
            }
        });

        removeBtn.setOnAction(event -> {
            try {
                SimCardService.getSimCardService().remove(Integer.parseInt(idTxt.getText()));
                PersonService.getPersonService().remove(Integer.parseInt(idTxt.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Removed Successfully", ButtonType.OK);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Removing Failed: " + e.getMessage(), ButtonType.OK);
                alert.show();
            }
        });

        personsTbl.setOnMouseReleased(event -> {
            Person person = personsTbl.getSelectionModel().getSelectedItem();
            idTxt.setText(String.valueOf(person.getId()));
            firstNameTxt.setText(person.getFirstName());
            lastNameTxt.setText(person.getLastName());
            nationalCodeTxt.setText(person.getNationalCode());

            if (person != null) {
                try {
                    refreshSimcardTable(SimCardService.getSimCardService().findByPersonNationalCode(person.getNationalCode()));
                    String count = String.valueOf(SimCardService.getSimCardService().countPersonSimCardsByNationalCode(person.getNationalCode()));
                    countTxt.setText(String.format("\"%s\" has %s SIM cards.", person.getNationalCode(), count));
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Finding By Person's NationalCode Failed: " + e.getMessage(), ButtonType.OK);
                    alert.show();
                }
            }
        });

    }

    private void resetForm() {
        try {
            idTxt.clear();
            firstNameTxt.clear();
            lastNameTxt.clear();
            nationalCodeTxt.clear();
            simcardNumberTxt.clear();
            countTxt.setText("");
            refreshPersonTable(PersonService.getPersonService().findAll());
            refreshSimcardTable(SimCardService.getSimCardService().findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setTitle("Error");
            alert.show();
        }
    }

    private void refreshPersonTable(List<Person> persons) {
        ObservableList<Person> observableList = FXCollections.observableList(persons);

        personIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        nationalCodeCol.setCellValueFactory(new PropertyValueFactory<>("nationalCode"));

        personsTbl.setItems(observableList);
    }

    private void refreshSimcardTable(List<SimCard> simcards) {
        ObservableList<SimCard> observableList = FXCollections.observableList(simcards);

        simcardIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        simcardNumberCol.setCellValueFactory(new PropertyValueFactory<>("simCardNumber"));

        simcardsTbl.setItems(observableList);
    }
}
