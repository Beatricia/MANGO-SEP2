package client.view.Admin.acceptEmployee;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import shared.Log;
import transferobjects.User;

import java.util.*;

public class AcceptEmployeeController implements ViewController
{
  @FXML private VBox employeeVBox; //javafx storage to show the pending employees

  private AcceptEmployeeViewModel viewModel;

  private final Map<User, Pane> usersInView = new HashMap<>(); //storing the users and the associated panes
                                                              // so its gonna be easier to remove one user's pane from the view

  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {

    viewModel = viewModelFactory.getAcceptEmployeeViewModel();
    viewModel.getEmployeeList().addListener(this::onListChange);

    onRefreshButtonPressed();
  }

  @Override public void refresh() {
    viewModel.refresh();
  }

  private void onListChange(ListChangeListener.Change<? extends User> change) {
    Platform.runLater(() -> {
      change.next();
      if(change.wasRemoved()){
        removeUsers(change.getRemoved());
        Log.log("AcceptEmployeeController the employee was removed from the ViewModel");
      }
      else if(change.wasAdded()){
        addUsers(change.getAddedSubList());
        Log.log("AcceptEmployeeController the employee was added to the ViewModel");
      }
    });
  }

  private void removeUsers(List<? extends User> users){
    Log.log("AcceptEmployeeController remove user pressed");
    for (User user : users){
      try{
        Pane p = usersInView.get(user);
        employeeVBox.getChildren().remove(p);
        usersInView.remove(user);
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  private void addUsers(List<? extends User> users){
    Log.log("AcceptEmployeeController add user pressed");
    for (User user : users){
      try{
        Pane p = createUserBox(user);
        usersInView.put(user, p);
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  private Pane createUserBox(User user){
    Label label = new Label(user.toString());

    Button buttonAccept = new Button("Accept");
    buttonAccept.setOnAction(evt -> viewModel.handleUser(user, true));
    Button buttonDecline = new Button("Decline");
    buttonDecline.setOnAction(evt -> viewModel.handleUser(user, false));

    HBox buttonBox = new HBox();
    buttonBox.setPadding(new Insets(2));
    buttonBox.getChildren().addAll(buttonAccept, buttonDecline);

    VBox centerVBox = new VBox();
    centerVBox.setAlignment(Pos.CENTER_LEFT);
    centerVBox.getChildren().add(label);


    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(centerVBox);
    borderPane.setRight(buttonBox);

    employeeVBox.getChildren().add(borderPane);

    Log.log("AcceptEmployeeController a new userBox is created");
    return borderPane;
  }

  public void onRefreshButtonPressed() {
    refresh();
  }
}


/*
   Scroll pane
┌───────────────────────────────────────────────────────────────────────┐
│                                                                       │
│    Vbox                                                               │
│   ┌────────────────────────────────────────────────────────────┐      │
│   │    Border pane                                             │      │
│   │ ┌────────────────────────────────────────────────────────┐ │      │
│   │ │        CENTER                     RIGHT                │ │      │
│   │ │                                                        │ │      │
│   │ │                          Hbox                          │ │      │
│   │ │                         ┌────────────────────────────┐ │ │      │
│   │ │  Vbox (align left)      │                            │ │ │      │
│   │ │    ┌────────────┐       │ ┌─────────┐   ┌─────────┐  │ │ │      │
│   │ │    │ Name       │       │ │  Accept │   │ Decline │  │ │ │      │
│   │ │    └────────────┘       │ └─────────┘   └─────────┘  │ │ │      │
│   │ │                         │                            │ │ │      │
│   │ │                         └────────────────────────────┘ │ │      │
│   │ │                                                        │ │      │
│   │ │                                                        │ │      │
│   │ └────────────────────────────────────────────────────────┘ │      │
│   │                                                            │      │
│   │    Border pane                                             │      │
│   │ ┌────────────────────────────────────────────────────────┐ │      │
│   │ │        CENTER                     RIGHT                │ │      │
│   │ │                                                        │ │      │
│   │ │                          Hbox                          │ │      │
│   │ │                         ┌────────────────────────────┐ │ │      │
│   │ │  Vbox (align left)      │                            │ │ │      │
│   │ │    ┌────────────┐       │ ┌─────────┐   ┌─────────┐  │ │ │      │
│   │ │    │ Name       │       │ │  Accept │   │ Decline │  │ │ │      │
│   │ │    └────────────┘       │ └─────────┘   └─────────┘  │ │ │      │
│   │ │                         │                            │ │ │      │
│   │ │                         └────────────────────────────┘ │ │      │
│   │ │                                                        │ │      │
│   │ │                                                        │ │      │
│   │ └────────────────────────────────────────────────────────┘ │      │
│   │                                                            │      │
│   │                                                            │      │
│   │                                                            │      │
│   │                                                            │      │
│   │                                                            │      │
│   │                                                            │      │
│   └────────────────────────────────────────────────────────────┘      │
│                                                                       │
└───────────────────────────────────────────────────────────────────────┘
 */




/*class TestViewModel extends AcceptEmployeeViewModel
{
  private final String[] surnames_list = new String[]{"Smith","Johnson","Williams","Brown","Jones","Miller","Davis","Garcia","Rodriguez","Wilson","Martinez","Anderson","Taylor","Thomas","Hernandez","Moore","Martin","Jackson","Thompson","White","Lopez","Lee","Gonzalez","Harris","Clark","Lewis","Robinson","Walker","Perez","Hall","Young","Allen","Sanchez","Wright","King","Scott","Green","Baker","Adams","Nelson","Hill","Ramirez","Campbell","Mitchell","Roberts","Carter","Phillips","Evans","Turner","Torres","Parker","Collins","Edwards","Stewart","Flores","Morris","Nguyen","Murphy","Rivera","Cook","Rogers","Morgan","Peterson","Cooper","Reed","Bailey","Bell","Gomez","Kelly","Howard","Ward","Cox","Diaz","Richardson","Wood","Watson","Brooks","Bennett","Gray","James","Reyes","Cruz","Hughes","Price","Myers","Long","Foster","Sanders","Ross","Morales","Powell","Sullivan","Russell","Ortiz","Jenkins","Gutierrez","Perry","Butler","Barnes","Fisher","Henderson","Coleman","Simmons","Patterson","Jordan","Reynolds","Hamilton","Graham","Kim","Gonzales","Alexander","Ramos","Wallace","Griffin","West","Cole","Hayes","Chavez","Gibson","Bryant","Ellis","Stevens","Murray","Ford","Marshall","Owens","Mcdonald","Harrison","Ruiz","Kennedy","Wells","Alvarez","Woods","Mendoza","Castillo","Olson","Webb","Washington","Tucker","Freeman","Burns","Henry","Vasquez","Snyder","Simpson","Crawford","Jimenez","Porter","Mason","Shaw","Gordon","Wagner","Hunter","Romero","Hicks","Dixon","Hunt","Palmer","Robertson","Black","Holmes","Stone","Meyer","Boyd","Mills","Warren","Fox","Rose","Rice","Moreno","Schmidt","Patel","Ferguson","Nichols","Herrera","Medina","Ryan","Fernandez","Weaver","Daniels","Stephens","Gardner","Payne","Kelley","Dunn","Pierce","Arnold","Tran","Spencer","Peters","Hawkins","Grant","Hansen","Castro","Hoffman","Hart","Elliott","Cunningham","Knight","Bradley","Carroll","Hudson","Duncan","Armstrong","Berry","Andrews","Johnston","Ray","Lane","Riley","Carpenter","Perkins","Aguilar","Silva","Richards","Willis","Matthews","Chapman","Lawrence","Garza","Vargas","Watkins","Wheeler","Larson","Carlson","Harper","George","Greene","Burke","Guzman","Morrison","Munoz","Jacobs","Obrien","Lawson","Franklin","Lynch","Bishop","Carr","Salazar","Austin","Mendez","Gilbert","Jensen","Williamson","Montgomery","Harvey","Oliver","Howell","Dean","Hanson","Weber","Garrett","Sims","Burton","Fuller","Soto","Mccoy","Welch","Chen","Schultz","Walters","Reid","Fields","Walsh","Little","Fowler","Bowman","Davidson","May","Day","Schneider","Newman","Brewer","Lucas","Holland","Wong","Banks","Santos","Curtis","Pearson","Delgado","Valdez","Pena","Rios","Douglas","Sandoval","Barrett","Hopkins","Keller","Guerrero","Stanley","Bates","Alvarado","Beck","Ortega","Wade","Estrada","Contreras","Barnett","Caldwell","Santiago","Lambert","Powers","Chambers","Nunez","Craig","Leonard","Lowe","Rhodes","Byrd","Gregory","Shelton","Frazier","Becker","Maldonado","Fleming","Vega","Sutton","Cohen","Jennings","Parks","Mcdaniel","Watts","Barker","Norris","Vaughn","Vazquez","Holt","Schwartz","Steele","Benson","Neal","Dominguez","Horton","Terry","Wolfe","Hale","Lyons","Graves","Haynes","Miles","Park","Warner","Padilla","Bush","Thornton","Mccarthy","Mann","Zimmerman","Erickson","Fletcher","Mckinney","Page","Dawson","Joseph","Marquez","Reeves","Klein","Espinoza","Baldwin","Moran","Love","Robbins","Higgins","Ball","Cortez","Le","Griffith","Bowen","Sharp","Cummings","Ramsey","Hardy","Swanson","Barber","Acosta","Luna","Chandler","Blair","Daniel","Cross","Simon","Dennis","Oconnor","Quinn","Gross","Navarro","Moss","Fitzgerald","Doyle","Mclaughlin","Rojas","Rodgers","Stevenson","Singh","Yang","Figueroa","Harmon","Newton","Paul","Manning","Garner","Mcgee","Reese","Francis","Burgess","Adkins","Goodman","Curry","Brady","Christensen","Potter","Walton","Goodwin","Mullins","Molina","Webster","Fischer","Campos","Avila","Sherman","Todd","Chang","Blake","Malone","Wolf","Hodges","Juarez","Gill","Farmer","Hines","Gallagher","Duran","Hubbard","Cannon","Miranda","Wang","Saunders","Tate","Mack","Hammond","Carrillo","Townsend","Wise","Ingram","Barton","Mejia","Ayala","Schroeder","Hampton","Rowe","Parsons","Frank","Waters","Strickland","Osborne","Maxwell","Chan","Deleon","Norman","Harrington","Casey","Patton","Logan","Bowers","Mueller","Glover","Floyd","Hartman","Buchanan","Cobb","French","Kramer","Mccormick","Clarke","Tyler","Gibbs","Moody","Conner","Sparks","Mcguire","Leon","Bauer","Norton","Pope","Flynn","Hogan","Robles","Salinas","Yates","Lindsey","Lloyd","Marsh","Mcbride","Owen","Solis","Pham","Lang","Pratt","Lara","Brock","Ballard","Trujillo","Shaffer","Drake","Roman","Aguirre","Morton","Stokes","Lamb","Pacheco","Patrick","Cochran","Shepherd","Cain","Burnett","Hess","Li","Cervantes","Olsen","Briggs","Ochoa","Cabrera","Velasquez","Montoya","Roth","Meyers","Cardenas","Fuentes","Weiss","Wilkins","Hoover","Nicholson","Underwood","Short","Carson","Morrow","Colon","Holloway","Summers","Bryan","Petersen","Mckenzie","Serrano","Wilcox","Carey","Clayton","Poole","Calderon","Gallegos","Greer","Rivas","Guerra","Decker","Collier","Wall","Whitaker","Bass","Flowers","Davenport","Conley","Houston","Huff","Copeland","Hood","Monroe","Massey","Roberson","Combs","Franco","Larsen","Pittman","Randall","Skinner","Wilkinson","Kirby","Cameron","Bridges","Anthony","Richard","Kirk","Bruce","Singleton","Mathis","Bradford","Boone","Abbott","Charles","Allison","Sweeney","Atkinson","Horn","Jefferson","Rosales","York","Christian","Phelps","Farrell","Castaneda","Nash","Dickerson","Bond","Wyatt","Foley","Chase","Gates","Vincent","Mathews","Hodge","Garrison","Trevino","Villarreal","Heath","Dalton","Valencia","Callahan","Hensley","Atkins","Huffman","Roy","Boyer","Shields","Lin","Hancock","Grimes","Glenn","Cline","Delacruz","Camacho","Dillon","Parrish","Oneill","Melton","Booth","Kane","Berg","Harrell","Pitts","Savage","Wiggins","Brennan","Salas","Marks","Russo","Sawyer","Baxter","Golden","Hutchinson","Liu","Walter","Mcdowell","Wiley","Rich","Humphrey","Johns","Koch","Suarez","Hobbs","Beard","Gilmore","Ibarra","Keith","Macias","Khan","Andrade","Ware","Stephenson","Henson","Wilkerson","Dyer","Mcclure","Blackwell","Mercado","Tanner","Eaton","Clay","Barron","Beasley","Oneal","Small","Preston","Wu","Zamora","Macdonald","Vance","Snow","Mcclain","Stafford","Orozco","Barry","English","Shannon","Kline","Jacobson","Woodard","Huang","Kemp","Mosley","Prince","Merritt","Hurst","Villanueva","Roach","Nolan","Lam","Yoder","Mccullough","Lester","Santana","Valenzuela","Winters","Barrera","Orr","Leach","Berger","Mckee","Strong","Conway","Stein","Whitehead","Bullock","Escobar","Knox","Meadows","Solomon","Velez","Odonnell","Kerr","Stout","Blankenship","Browning","Kent","Lozano","Bartlett","Pruitt","Buck","Barr","Gaines","Durham","Gentry","Mcintyre","Sloan","Rocha","Melendez","Herman","Sexton","Moon","Hendricks","Rangel","Stark","Lowery","Hardin","Hull","Sellers","Ellison","Calhoun","Gillespie","Mora","Knapp","Mccall","Morse","Dorsey","Weeks","Nielsen","Livingston","Leblanc","Mclean","Bradshaw","Glass","Middleton","Buckley","Schaefer","Frost","Howe","House","Mcintosh","Ho","Pennington","Reilly","Hebert","Mcfarland","Hickman","Noble","Spears","Conrad","Arias","Galvan","Velazquez","Huynh","Frederick","Randolph","Cantu","Fitzpatrick","Mahoney","Peck","Villa","Michael","Donovan","Mcconnell","Walls","Boyle","Mayer","Zuniga","Giles","Pineda","Pace","Hurley","Mays","Mcmillan","Crosby","Ayers","Case","Bentley","Shepard","Everett","Pugh","David","Mcmahon","Dunlap","Bender","Hahn","Harding","Acevedo","Raymond","Blackburn","Duffy","Landry","Dougherty","Bautista","Shah","Potts","Arroyo","Valentine","Meza","Gould","Vaughan","Fry","Rush","Avery","Herring","Dodson","Clements","Sampson","Tapia","Bean","Lynn","Crane","Farley","Cisneros","Benton","Ashley","Mckay","Finley","Best","Blevins","Friedman","Moses","Sosa","Blanchard","Huber","Frye","Krueger","Bernard","Rosario","Rubio","Mullen","Benjamin","Haley","Chung","Moyer","Choi","Horne","Yu","Woodward","Ali","Nixon","Hayden","Rivers","Estes","Mccarty","Richmond","Stuart","Maynard","Brandt","Oconnell","Hanna","Sanford","Sheppard","Church","Burch","Levy","Rasmussen","Coffey","Ponce","Faulkner","Donaldson","Schmitt","Novak","Costa","Montes","Booker","Cordova","Waller","Arellano","Maddox","Mata","Bonilla","Stanton","Compton","Kaufman","Dudley","Mcpherson","Beltran","Dickson","Mccann","Villegas","Proctor","Hester","Cantrell","Daugherty","Cherry","Bray","Davila","Rowland","Madden","Levine","Spence","Good","Irwin","Werner","Krause","Petty","Whitney","Baird","Hooper","Pollard","Zavala","Jarvis","Holden","Haas","Hendrix","Mcgrath","Bird","Lucero","Terrell","Riggs","Joyce","Mercer","Rollins","Galloway","Duke","Odom","Andersen","Downs","Hatfield","Benitez","Archer","Huerta","Travis","Mcneil","Hinton","Zhang","Hays","Mayo","Fritz","Branch","Mooney","Ewing","Ritter","Esparza","Frey","Braun","Gay","Riddle","Haney","Kaiser","Holder","Chaney","Mcknight","Gamble","Vang","Cooley","Carney","Cowan","Forbes","Ferrell","Davies","Barajas","Shea","Osborn","Bright","Cuevas","Bolton","Murillo","Lutz","Duarte","Kidd","Key","Cooke"};

  private final ObservableList<User> users = FXCollections.observableList(new ArrayList<>());


  @Override public void handleUser(User user, boolean accept){
    System.out.println(user + " got " + (accept ? "accepted" : "declined"));
    users.remove(user);
  }

  @Override public void refresh(){
    users.clear();

    Random r = new Random();
    int usercount = r.nextInt(30) + 1; //how many users to create

    for (int i = 0; i < usercount; i++) {

      int userId = r.nextInt(2000); // create user id

      String username = "user" + userId;
      String firstName = surnames_list[r.nextInt(surnames_list.length)];
      String lastName = surnames_list[r.nextInt(surnames_list.length)];

      User user = new User(username, UserType.EMPLOYEE, firstName, lastName);

      users.add(user);
    }
  }

  @Override public ObservableList<User> getEmployeeList(){
    return users;
  }
}*/
