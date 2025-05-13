// MainActivity.kt
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MammaG",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = { navController.navigate("registration") },
            modifier = Modifier.width(200.dp)
        ) {
            Text("NEXT")
        }
    }
}

val registrationSteps = listOf("Account", "Personal", "Baby", "Confirm")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavHostController) {
    var currentStep by rememberSaveable { mutableStateOf(0) }
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Stepper corrigé
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            registrationSteps.forEachIndexed { index, _ ->
                StepIndicator(
                    number = index + 1,
                    isActive = index <= currentStep,
                    modifier = Modifier.weight(1f)
                )

                if (index < registrationSteps.lastIndex) {
                    Spacer(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(4.dp)
                            .background(
                                color = if (index < currentStep) MaterialTheme.colorScheme.primary
                                else Color.LightGray,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        when(currentStep) {
            0 -> AccountStep(
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it }
            )
            1 -> PersonalStep(name = name, onNameChange = { name = it })
            2 -> BabyStep() // À implémenter
            3 -> ConfirmStep() // À implémenter
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if(currentStep < registrationSteps.lastIndex) {
                    currentStep++
                } else {
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if(currentStep == registrationSteps.lastIndex) "Confirm" else "Next")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Already have an account? Log in")
        }
    }
}

@Composable
fun StepIndicator(number: Int, isActive: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                color = if(isActive) MaterialTheme.colorScheme.primary
                else Color.LightGray
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = if(isActive) Color.White else Color.DarkGray
        )
    }
}

@Composable
fun AccountStep(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Email, null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Lock, null) }
        )
    }
}

@Composable
fun PersonalStep(name: String, onNameChange: (String) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Person, null) }
        )
    }
}

// Placeholders pour les étapes manquantes
@Composable
fun BabyStep() {
    Text("Baby Step (À implémenter)")
}

@Composable
fun ConfirmStep() {
    Text("Confirm Step (À implémenter)")
}