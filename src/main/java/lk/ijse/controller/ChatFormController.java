package lk.ijse.controller;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ChatFormController implements Initializable {
    public Pane paneCamImgBtns;
    public BorderPane paneImage;
    public Pane paneEmojies;
    public ImageView imgView;
    public JFXButton btnImageCapture;
    public JFXButton btnSendImg;
    @FXML
    private JFXButton btnSend;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane flowPaneEmojiSet1;
    @FXML
    private FlowPane flowPaneEmojiSet2;
    @FXML
    private FlowPane flowPaneEmojiSet3;
    @FXML
    private VBox vBoxChat;

    @FXML
    private JFXTextField txtField;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private Webcam webcam;
    private String userName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName = LoginFormController.userName;

        new Thread(() -> {
            try{
                socket = new Socket("localhost", 1234);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF16"));
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF16"));

                sendMessage(userName);

                while (true){
                    messageListner();
                }

            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                new Alert(Alert.AlertType.ERROR, "Client connectring to server error!").show();
            }

        }).start();

        webcam = Webcam.getDefault();
        webcam.setViewSize(new java.awt.Dimension(320,240));

        vBoxChat.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scrollPane.setVvalue((Double) t1);
            }
        });

        btnSend.setOnAction((e) -> {
            txtFieldOnAction(e);
        });

        new Thread(() -> {
            loadEmojis();
        }).start();

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    private void loadEmojis() {
        String[] emojiUnicodeSet1 = {
                "\uD83D\uDE00",
                "\uD83D\uDE03",
                "\uD83D\uDE04",
                "\uD83D\uDE0A",
                "\uD83D\uDE06",
                "\uD83D\uDE05",
                "\uD83E\uDD23",
                "\uD83D\uDE02",
                "\uD83D\uDE0D",
                "\uD83D\uDE43",
                "\uD83D\uDE09",
                "\uD83D\uDE18",
                "\uD83D\uDE0C",
                "\uD83E\uDD2A",
                "\uD83D\uDE0F",
                "\uD83D\uDE07",
                "\uD83D\uDE3A",
                "\uD83D\uDE0B",
                "\uD83D\uDE1C",
                "\uD83D\uDE1D",
                "\uD83D\uDE18",
                "\uD83D\uDE1B",
                "\uD83E\uDD2C",
                "\uD83E\uDD73",
                "\uD83D\uDE4F",
                "\uD83D\uDC42",
                "\uD83D\uDC43",
                "\uD83D\uDC40",
                "\uD83D\uDC41",
                "\uD83D\uDC68",
                "\uD83D\uDC69",
                "\uD83D\uDC67",
                "\uD83D\uDC66",
                "\uD83D\uDC65",
                "\uD83D\uDC64",
                "\uD83D\uDC63",
                "\uD83D\uDC6B",
                "\uD83D\uDC6A",
                "\uD83D\uDC8B",
                "\uD83D\uDC49",
                "\uD83D\uDC48",
                "\uD83D\uDC4A",
                "\uD83D\uDC4D",
                "\uD83D\uDC46",
                "\uD83D\uDC47",
                "\uD83D\uDC4C",
                "\uD83D\uDC4E",
                "\uD83D\uDC4F",
                "\uD83D\uDC4B",
                "\uD83D\uDC50",
                "\uD83D\uDC51",
                "\uD83D\uDC52",
                "\uD83D\uDC55",
                "\uD83D\uDC56",
                "\uD83D\uDC57",
                "\uD83D\uDC53",
                "\uD83D\uDC54",
                "\uD83D\uDCBC",
                "\uD83D\uDC8D",
                "\uD83D\uDC8E",
                "\uD83D\uDC8C",
                "\uD83D\uDC94",
                "\uD83D\uDC97",
                "\uD83D\uDC95",
                "\uD83D\uDC96",
                "\uD83D\uDC93",
                "\uD83D\uDC9E",
                "\uD83D\uDC8F",
                "\uD83D\uDC83",
                "\uD83D\uDC7A",
                "\uD83D\uDC79",
                "\uD83D\uDC78",
                "\uD83D\uDC77",
                "\uD83D\uDC76",
                "\uD83D\uDC75",
                "\uD83D\uDC74",
                "\uD83D\uDC71",
                "\uD83D\uDC72",
                "\uD83D\uDC73",
                "\uD83D\uDC70",
                "\uD83D\uDC92",
                "\uD83D\uDC83",
                "\uD83D\uDC88",
                "\uD83D\uDC85",
                "\uD83D\uDC5E",
                "\uD83D\uDC87",
                "\uD83D\uDC70",
                "\uD83D\uDC63",
                "\uD83D\uDC66",
                "\uD83D\uDC67",
                "\uD83D\uDC69",
                "\uD83D\uDC68",
                "\uD83D\uDC71",
                "\uD83D\uDC6E",
                "\uD83D\uDC6D",
                "\uD83D\uDC72",
                "\uD83D\uDC73",
                "\uD83D\uDC6F",
                "\uD83D\uDC91",
                "\uD83D\uDC86",
                "\uD83D\uDC6B",
                "\uD83D\uDC6C",
                "\uD83D\uDC6A",
                "\uD83D\uDC89",
                "\uD83D\uDC8A",
                "\uD83D\uDC83",
                "\uD83D\uDC84",
                "\uD83D\uDC85",
                "\uD83D\uDC80",
                "\uD83D\uDC8C",
                "\uD83D\uDC8D",
                "\uD83D\uDC8B",
                "\uD83D\uDC8E",
                "\uD83D\uDC87",
                "\uD83D\uDC88",
                "\uD83D\uDC89",
                "\uD83D\uDC8A",
                "\uD83D\uDC93",
                "\uD83D\uDC94",
                "\uD83D\uDC95",
                "\uD83D\uDC96",
                "\uD83D\uDC97",
                "\uD83D\uDC9E",
                "\uD83D\uDC9F",
                "\uD83D\uDC9D",
                "\uD83D\uDC4A",
                "\uD83D\uDC4B",
                "\uD83D\uDC4C",
                "\uD83D\uDC4D",
                "\uD83D\uDC4E",
                "\uD83D\uDC4F",
                "\uD83D\uDC50",
                "\uD83D\uDC51",
                "\uD83D\uDC52",
                "\uD83D\uDC53",
                "\uD83D\uDC54",
                "\uD83D\uDC55",
                "\uD83D\uDC56",
                "\uD83D\uDC57",
                "\uD83D\uDC58",
                "\uD83D\uDC59",
                "\uD83D\uDC5A",
                "\uD83D\uDC5B",
                "\uD83D\uDC5C",
                "\uD83D\uDC5D",
                "\uD83D\uDC9A",
                "\uD83D\uDC9B"
        };

        for (int i = 0; i < emojiUnicodeSet1.length; i++) {
            Text text = new Text(emojiUnicodeSet1[i]);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setPadding(new Insets(10, 0 , 0, 0));
            textFlow.getStylesheets().add("/styles/styles.css");
            textFlow.getStyleClass().add("text-field");

            textFlowOnMouseClicked(textFlow, emojiUnicodeSet1[i]);

            flowPaneEmojiSet1.getChildren().add(textFlow);
        }

        String[] emojiUnicodeSet2 = {
                "\uD83E\uDD73", "\u2602\uFE0F", "\u26C6", "\uD83D\uDD28", "\uD83E\uDDE1", // 🧳 🌂 ☂️ 🧵 🪡
                "\uD83E\uDDE2", "\uD83D\uDC61", "\uD83E\uDDE3", "\uD83D\uDC53", "\uD83D\uDD76", // 🪢 🪭 🧶 👓 🕶
                "\uD83E\uDD7D", "\uD83E\uDEE0", "\uD83D\uDC5A", "\uD83D\uDC56", "\uD83D\uDC55", // 🥽 🥼 🦺 👔 👕
                "\uD83E\uDD36", "\uD83D\uDC64", "\uD83D\uDC62", "\uD83D\uDC63", "\uD83E\uDDE5", // 👖 🧣 🧤 🧥 🧦
                "\uD83D\uDC57", "\uD83E\uDE72", "\uD83E\uDE71", "\uD83D\uDC59", "\uD83E\uDE73", // 👗 🥻 🩴 🩱 🩳
                "\uD83D\uDC58", "\uD83E\uDE7B", "\uD83D\uDC5D", "\uD83D\uDC5C", "\uD83D\uDC5B", // 👙 👚 👛 👜 👝
                "\uD83C\uDF92", "\uD83D\uDC5E", "\uD83D\uDC5F", "\uD83E\uDE7E", "\uD83D\uDC60", // 🎒 👞 👟 🥿 👠
                "\uD83D\uDC61", "\uD83E\uDDEB", "\uD83D\uDC62", "\uD83D\uDC51", "\uD83D\uDC52", // 👡 🧫 👢 👑 👒
                "\uD83C\uDF93", "\uD83C\uDFA9", "\uD83D\uDCAE", "\uD83D\uDCBB", "\uD83D\uDEB4", // 🎓 🧢 ⛑ 🪖 💄
                "\uD83D\uDC8B", "\uD83D\uDC85" // 💋 💅
        };

        for (int i = 0; i < emojiUnicodeSet2.length; i++) {
            Text text = new Text(emojiUnicodeSet2[i]);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setPadding(new Insets(10, 0 , 0, 0));
            textFlow.getStylesheets().add("/styles/styles.css");
            textFlow.getStyleClass().add("text-field");

            textFlowOnMouseClicked(textFlow, emojiUnicodeSet2[i]);

            flowPaneEmojiSet2.getChildren().add(textFlow);
        }

        String[] emojiUnicodeSet3 = {
                "\uD83D\uDC36", "\uD83D\uDC31", "\uD83D\uDC2D", "\uD83D\uDC39", "\uD83D\uDC30", // 🐶 🐱 🐭 🐹 🐰
                "\uD83E\uDD8A", "\uD83D\uDC3B", "\uD83D\uDC3C", "\uD83E\uDDFB", "\uD83D\uDC28", // 🦊 🐻 🐼 🐻‍❄️ 🐨
                "\uD83D\uDC2F", "\uD83D\uDC41", "\uD83E\uDD8B", "\uD83D\uDC37", "\uD83D\uDC3D", // 🐯 🦁 🐮 🐷 🐽
                "\uD83D\uDC38", "\uD83D\uDC35", "\uD83D\uDE48", "\uD83D\uDE49", "\uD83D\uDC12", // 🐸 🐵 🙈 🙉 🙊
                "\uD83D\uDC12", "\uD83D\uDC25", "\uD83D\uDC24", "\uD83D\uDC24\uFE0F", "\uD83D\uDC23", // 🐒 🐔 🐧 🐦 🐦‍⬛
                "\uD83D\uDC25", "\uD83D\uDC23", "\uD83D\uDC24", "\uD83D\uDC16", "\uD83D\uDC17", // 🐤 🐣 🐥 🦆 🦅
                "\uD83D\uDC49", "\uD83D\uDC38", "\uD83D\uDC34", "\uD83D\uDC3C", "\uD83D\uDC04", // 🦉 🦇 🐺 🐗 🐴
                "\uD83E\uDD84", "\uD83D\uDC1D", "\uD83D\uDC22", "\uD83D\uDC30", "\uD83D\uDC0D", // 🦄 🐝 🪱 🐛 🦋
                "\uD83D\uDC1C", "\uD83D\uDC1C\uFE0F", "\uD83D\uDC0A", "\uD83D\uDC12", "\uD83D\uDC13", // 🐌 🐞 🐜 🪰 🪲
                "\uD83D\uDC73", "\uD83D\uDC1E", "\uD83D\uDC1F", "\uD83D\uDC1B", "\uD83D\uDC1B\uFE0F", // 🕷 🕸 🦂 🐢 🐍
                "\uD83E\uDD99", "\uD83D\uDC19", "\uD83E\uDD9A", "\uD83E\uDD9E", "\uD83D\uDC19", // 🦎 🦖 🦕 🐙 🦑
                "\uD83D\uDC90", "\uD83D\uDC0E", "\uD83D\uDC0F", "\uD83D\uDC1E", "\uD83D\uDC0F", // 🦐 🦞 🦀 🪼 🪸
                "\uD83D\uDC21", "\uD83D\uDC24", "\uD83D\uDC26", "\uD83D\uDC0F", "\uD83D\uDC22", // 🐡 🐠 🐟 🐬 🐳
                "\uD83D\uDC0B", "\uD83D\uDC0B\uFE0F", "\uD83D\uDC0B", "\uD83D\uDC0E", "\uD83D\uDC12", // 🐋 🦈 🐊 🐅 🐆
                "\uD83E\uDD8F", "\uD83E\uDDBF", "\uD83E\uDDA3", "\uD83E\uDDA4", "\uD83D\uDC18", // 🦓 🫏 🦍 🦣 🐘
                "\uD83E\uDDAC", "\uD83E\uDD9B", "\uD83D\uDC99", "\uD83E\uDDAD", "\uD83E\uDD98", // 🦛 🦏 🐪 🐫 🦒
                "\uD83E\uDDA0", "\uD83D\uDC02", "\uD83D\uDC02\u200D\uD83D\uDC27", "\uD83D\uDC08", "\uD83D\uDC08\uFE0F", // 🦘 🦬 🐃 🐂 🐄
                "\uD83D\uDC0E", "\uD83D\uDC0F", "\uD83D\uDC11", "\uD83D\uDC09", "\uD83D\uDC08", // 🐎 🐖 🐏 🐑 🦙
                "\uD83D\uDC10", "\uD83D\uDC34", "\uD83D\uDC15", "\uD83D\uDC28", "\uD83D\uDC29", // 🐐 🦌 🫎 🐕 🐩
                "\uD83D\uDC36", "\uD83D\uDC31", "\uD83E\uDDAE", "\uD83D\uDC31\u200D\uD83D\uDC1F", "\uD83D\uDCAD", // 🦮 🐕‍🦺 🐈 🐈‍⬛ 🪽
                "\uD83D\uDC13", "\uD83E\uDD83", "\uD83D\uDC13", "\uD83D\uDC1A", "\uD83D\uDC1A\uFE0F", // 🐓 🦃 🦤 🦚 🦜
                "\uD83E\uDDA2", "\uD83D\uDCFF", "\uD83D\uDC9A", "\uD83D\uDC9C", "\uD83E\uDDFF", // 🦢 🪿 🦩 🕊 🐇
                "\uD83E\uDD9D", "\uD83E\uDDA8", "\uD83D\uDC9E", "\uD83E\uDDA5", "\uD83E\uDDA6", // 🦝 🦨 🦡 🦫 🦦
                "\uD83E\uDD8A", "\uD83E\uDDA6", "\uD83D\uDC01", "\uD83D\uDC01\uFE0F", "\uD83D\uDC2D", // 🦥 🐁 🐀 🐿 🦔
                "\uD83D\uDC09", "\uD83D\uDC32", "\uD83C\uDF35", "\uD83D\uDC32", "\uD83C\uDF35", // 🐾 🐉 🐲 🌵 🎄
                "\uD83C\uDF32", "\uD83D\uDEBF", "\uD83C\uDF33", "\uD83D\uDC38", "\uD83D\uDEBF", // 🌲 🌳 🌴 🪹 🪺
                "\uD83D\uDE3A", "\uD83D\uDE3A\uFE0F", "\uD83C\uDF3A", "\uD83D\uDC38", "\uD83C\uDF37", // 🪵 🌱 🌿 ☘️ 🍀
                "\uD83C\uDF8D", "\uD83C\uDF8B", "\uD83C\uDF8A", "\uD83C\uDFB4", "\uD83D\uDD25", // 🎍 🪴 🎋 🍃 🍂
                "\uD83C\uDF44", "\uD83C\uDFDB", "\uD83C\uDF3C", "\uD83D\uDC0C", "\uD83C\uDF40", // 🍄 🐚 🪨 🌾 💐
                "\uD83C\uDF37", "\uD83C\uDF3D", "\uD83C\uDF38", "\uD83D\uDD25", "\uD83C\uDF3F", // 🌷 🪷 🌹 🥀 🌺
                "\uD83C\uDF3E", "\uD83C\uDF3B", "\uD83C\uDF3B\uFE0F", "\uD83C\uDF7B", "\uD83C\uDF37", // 🪻 🌼 🌻 🌞 🌝
                "\uD83C\uDF1F", "\uD83C\uDF0E", "\uD83C\uDF1A", "\uD83C\uDF19", "\uD83C\uDF11", // 🌛 🌜 🌚 🌕 🌖
                "\uD83C\uDF13", "\uD83C\uDF11", "\uD83C\uDF12", "\uD83C\uDF13", "\uD83C\uDF16", // 🌗 🌘 🌑 🌒 🌓
                "\uD83C\uDF14", "\uD83C\uDF15", "\uD83C\uDF03", "\uD83C\uDF04", "\uD83C\uDF1A", // 🌔 🌙 🌎 🌍 🌏
                "\uD83D\uDE90", "\u2B50", "\uD83C\uDF1F", "\u2728", "\u2728", // 🪐 💫 ⭐️ 🌟 ✨
                "\u26A1", "\u2604", "\uD83D\uDD25", "\u2601", "\u2601", // ⚡️ ☄️ 💥 🔥 🌪
                "\uD83C\uDF08", "\u2600", "\uD83C\uDF24", "\u26C5", "\u2601", // 🌈 ☀️ 🌤 ⛅️ 🌥
                "\u2601", "\uD83C\uDF26", "\u26C8", "\u26A1", "\uD83C\uDF27", // ☁️ 🌦 🌧 ⛈ 🌩
                "\uD83D\uDCA8", "\u2744", "\u2603", "\u26C6", "\uD83C\uDF27", // 🌨 ❄️ ☃️ ⛄️ 🌬
                "\uD83D\uDCA7", "\uD83D\uDCA8", "\uD83C\uDFE0", "\uD83D\uDCA8", "\uD83D\uDCA7", // 💧 💦 🫧 ☔️ ☂️
                "\uD83C\uDF0A" // 🌊
        };

//        for (int i = 0; i < emojiUnicodeSet3.length; i++) {
//            Text text = new Text(emojiUnicodeSet3[i]);
//            TextFlow textFlow = new TextFlow(text);
//            textFlow.setPadding(new Insets(10, 0 , 0, 0));
//            textFlow.getStylesheets().add("/styles/styles.css");
//            textFlow.getStyleClass().add("text-field");
//
//            textFlowOnMouseClicked(textFlow, emojiUnicodeSet3[i]);
//
//            flowPaneEmojiSet3.getChildren().add(textFlow);
//        }

        String[] emojis = {
                "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐨",
                "🐯", "🦁", "🐮", "🐷", "🐽", "🐸", "🐵", "🙈", "🙉", "🙊", "🐒",
                "🐔", "🐧", "🐦", "🐤", "🐣", "🐥", "🦆", "🦅", "🦉", "🦇",
                "🐺", "🐗", "🐴", "🦄", "🐝", "🪱", "🐛", "🦋", "🐌", "🐞", "🐜",
                "🪰", "🪲", "🪳", "🦟", "🦗", "🕷", "🕸", "🦂", "🐢", "🐍", "🦎",
                "🦖", "🦕", "🐙", "🦑", "🦐", "🦞", "🦀", "🪼", "🪸", "🐡", "🐠",
                "🐟", "🐬", "🐳", "🐋", "🦈", "🐊", "🐅", "🐆", "🦓", "🫏", "🦍",
                "🦧", "🦣", "🐘", "🦛", "🦏", "🐪", "🐫", "🦒", "🦘", "🦬", "🐃",
                "🐂", "🐄", "🐎", "🐖", "🐏", "🐑", "🦙", "🐐", "🦌", "🫎", "🐕",
                "🐩", "🦮", "🐈", "🪽", "🪶", "🐓", "🦃", "🦤", "🦚",
                "🦜", "🦢", "🪿", "🦩", "🕊", "🐇", "🦝", "🦨", "🦡", "🦫", "🦦",
                "🦥", "🐁", "🐀", "🐿", "🦔", "🐾", "🐉", "🐲", "🌵", "🎄", "🌲",
                "🌳", "🌴", "🪹", "🪺", "🪵", "🌱", "🌿", "☘️", "🍀", "🎍", "🪴",
                "🎋", "🍃", "🍂", "🍁", "🍄", "🐚", "🪨", "🌾", "💐", "🌷", "🪷",
                "🌹", "🥀", "🌺", "🌸", "🪻", "🌼", "🌻", "🌞", "🌝", "🌛", "🌜",
                "🌚", "🌕", "🌖", "🌗", "🌘", "🌑", "🌒", "🌓", "🌔", "🌙", "🌎",
                "🌍", "🌏", "🪐", "💫", "🌟", "✨", "💥", "🔥",
                "🌪", "🌈", "🌤", "🌥", "🌦", "🌧", "⛈", "🌩",
                "🌨", "🌬", "💨", "💧", "💦", "🫧",
                "🌊", "🌫"
        };

        Set<String> emojiSet = new HashSet<>(Arrays.asList(emojis));

        for (String unicode : emojiSet){
            Text text = new Text(unicode);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setPadding(new Insets(10, 0 , 0, 0));
            textFlow.getStylesheets().add("/styles/styles.css");
            textFlow.getStyleClass().add("text-field");

            textFlowOnMouseClicked(textFlow, unicode);

            flowPaneEmojiSet3.getChildren().add(textFlow);
        }
    }

    private void textFlowOnMouseClicked(TextFlow textFlow, String unicode) {
        textFlow.setOnMouseClicked((e) -> {
            txtField.appendText(unicode);
        });
    }

    @FXML
    void txtFieldOnAction(ActionEvent event) {
        String message = txtField.getText();
        if (message.isBlank()){
            new Alert(Alert.AlertType.WARNING, "There is no message to send!").show();
            return;
        }

        try {
            sendMessage(message);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            new Alert(Alert.AlertType.ERROR, "Message Sending Error!").show();
        }

        setMessage(message, Side.RIGHT);
        txtField.setText(null);
    }

    public void sendMessage(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void messageListner() throws IOException {
        String message = bufferedReader.readLine();
        String[] split = message.split(":");

        if (split[0].equals("_coming_an_image")){
            String sender = split[1];
            imageListner(sender);
        }else {
            Platform.runLater(() -> {
                setMessage(message, Side.LEFT);
            });
        }
    }

    private void imageListner(String senderUserName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        BufferedImage bufferedImage = ImageIO.read(bis);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        setImage(image, senderUserName, Side.LEFT);
    }


    @FXML
    void btnEmojiOnAction(ActionEvent event) {
        paneEmojies.setVisible(!paneEmojies.isVisible());
    }

    @FXML
    void btnImageOnAction(ActionEvent event) {
        paneCamImgBtns.setVisible(!paneCamImgBtns.isVisible());
    }

    private void setMessage(String message, Side side){
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 10, 5, 10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setMaxWidth(500);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        if (side.equals(Side.LEFT)){
            hBox.setAlignment(Pos.CENTER_LEFT);
            textFlow.setStyle(
                    "-fx-background-color: #a6c4ff;" +
                    "-fx-background-radius: 5px 20px 20px 20px;" +
                    "-fx-font-family: 'Segoe UI Emoji';" +
                    "-fx-font-size: 18px;");
//            textFlow.getStylesheets().add("/styles/textFlow.css");
//            textFlow.getStyleClass().add("text-flow");

        }else {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            textFlow.setStyle(
                    "-fx-background-color: #6affce;" +
                    "-fx-background-radius: 20px 5px 20px 20px;" +
                    "-fx-font-family: 'Segoe UI Emoji';" +
                    "-fx-font-size: 18px;");
//            textFlow.getStylesheets().add("/styles/textFlow.css");
//            textFlow.getStyleClass().add("text-flow");
        }

        hBox.getChildren().add(textFlow);
        vBoxChat.getChildren().add(hBox);
    }

    private void setImage(Image image, String senderUserName, Side side) {
        Text text = new Text(senderUserName);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 5 ,10));
        ImageView imageView = resizeImage(image, 400);
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        if (side.equals(Side.LEFT)){
            vBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setAlignment(Pos.CENTER_LEFT);
            textFlow.setMaxWidth(senderUserName.length() * 17);
            hBox.setPadding(new Insets(5, 10, 5, 10));
            textFlow.setStyle(
                    "-fx-background-color: #a6c4ff;" +
                            "-fx-background-radius: 5px 10px 0px 0px;" +
                            "-fx-start-margin: 10px;" +
                            "-fx-font-family: 'Segoe UI Emoji';" +
                            "-fx-font-size: 18px;");

            vBox.getChildren().add(textFlow);
            vBox.getChildren().add(imageView);
        }else {
            vBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            vBox.setPadding(new Insets(5, 10, 5, 10));
            hBox.setPadding(new Insets(5, 10, 5, 10));

//            textFlow.setMaxWidth(senderUserName.length() * 15);
//            textFlow.setStyle(
//                    "-fx-background-color: rgba(53,241,57,0.99);" +
//                            "-fx-background-radius: 20px 5px 0px 0px;" +
//                            "-fx-font-size: 16px;");

            vBox.getChildren().add(imageView);
        }

        hBox.getChildren().add(vBox);

        Platform.runLater(() -> {
            vBoxChat.getChildren().add(hBox);
        });


    }

    private java.awt.Image convertToAWTImage(Image fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        SwingFXUtils.fromFXImage(fxImage, bufferedImage);

        return bufferedImage;
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if (socket != null){
                socket.close();
            }

            if (bufferedReader != null){
                bufferedReader.close();
            }

            if (bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void btnCameraOnAction(ActionEvent actionEvent) {
        paneCamImgBtns.setVisible(false);
        paneImage.setVisible(!paneImage.isVisible());
        webcam.open();
        btnImageCapture.setVisible(true);
        btnSendImg.setDisable(true);

        new Thread(() -> {
            while (webcam.isOpen()){
                imgView.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
                try{Thread.sleep(20);} catch (InterruptedException e){e.printStackTrace();}
            }
        }).start();

    }

    public void btnImageChooserOnAction(ActionEvent actionEvent) {
        paneCamImgBtns.setVisible(false);
        paneImage.setVisible(!paneImage.isVisible());
        btnSendImg.setDisable(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG image","*.jpg"),
                new FileChooser.ExtensionFilter("PNG image", "*.png"),
                new FileChooser.ExtensionFilter("All images", "*.jpg", "*.png")
        );
        fileChooser.setInitialDirectory(new File("C:\\"));
        File file = fileChooser.showOpenDialog(new Stage());

        String filePath = file.getPath();

        imgView.setImage(new Image(filePath));
        btnSendImg.setDisable(false);
    }

    public void btnCloseOnActioin(ActionEvent actionEvent) {
        if (webcam.isOpen()){
            webcam.close();
        }

        btnImageCapture.setVisible(false);
        paneImage.setVisible(false);
        imgView.setImage(null);
    }

    public void btnImageCaptureOnAction(ActionEvent actionEvent) {
        webcam.close();
        btnSendImg.setDisable(false);


    }

    public void btnSendImgOnAction(ActionEvent actionEvent) {
        btnImageCapture.setVisible(false);

        try {
            bufferedWriter.write("_coming_an_image");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            Image fxImage = imgView.getImage();
            java.awt.Image awtImage = convertToAWTImage(fxImage);

            BufferedImage bufferedImage = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null),BufferedImage.TYPE_INT_ARGB);

            Graphics graphics = bufferedImage.createGraphics();
            graphics.drawImage(awtImage, 0, 0, null);
            graphics.dispose();

            ImageIO.write(bufferedImage,"png", bos);

            setImage(fxImage, userName, Side.RIGHT);

        } catch (IOException e){
            e.printStackTrace();
        }

        paneImage.setVisible(false);
        btnSendImg.setDisable(true);
    }

    public ImageView resizeImage(Image highResImage, double maxWidth) {
        double originalWidth = highResImage.getWidth();
        if (originalWidth <= maxWidth){
            return new ImageView(highResImage);
        }
        double originalHeight = highResImage.getHeight();

        double ratio = originalWidth / originalHeight;

        double newWidth = Math.min(originalWidth, maxWidth);
        double newHeight = newWidth / ratio;

        ImageView iv = new ImageView(highResImage);
        iv.setFitHeight(newHeight);
        iv.setFitWidth(newWidth);

        return iv;
    }
}
