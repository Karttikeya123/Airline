import java.io.File

data class BookingDetails(
    val name: String,
    val travelDate: String,
    val source: String,
    val destination: String,
    val price: Double,
    val customerId: Int
)

interface CustomerDetails {
    val name: String
    val travelDate: String
    val source: String
    val destination: String
    val price: Double
}

class Booking(private val customerDetails: CustomerDetails) {
    private val customerId = generateCustomerId()

    private fun generateCustomerId(): Int {
        return (1000..9999).random()
    }

    fun bookTicket() {
        println("Terms and Conditions:")
        println("1. Ticket can be cancelled only within 24 hours.")
        println("2. No refunds for cancellations made after 24 hours.")
        println("3. Payment should be made within 30 minutes of booking.")

        println("Booking Details:")
        customerDetails.display()
        println("Customer ID: $customerId")

        val bookingDetails = BookingDetails(
            customerDetails.name,
            customerDetails.travelDate,
            customerDetails.source,
            customerDetails.destination,
            customerDetails.price,
            customerId
        )

        saveBookingToFile(bookingDetails)
    }

    fun getCustomerId(): Int {
        return customerId
    }
}

class Cancellation {
    fun cancelTicket(customerId: Int) {
        println("Do you want to cancel the ticket with Customer ID $customerId? (Yes/No): ")
        val userResponse = readLine()
        if (userResponse?.equals("Yes", ignoreCase = true) == true) {
            println("Ticket with Customer ID $customerId has been canceled.")
        } else {
            println("Ticket cancellation request canceled.")
        }
    }
}

class TransactionManagementSystem {
    fun processPayment(paymentMethod: String) {
        println("Processing payment via $paymentMethod...")
        println("Payment successful!")

        val cancellation = Cancellation()
        println("Enter the customer ID to cancel the ticket if wanted:")
        val customerIdToCancel = readLine()?.toIntOrNull() ?: 0
        cancellation.cancelTicket(customerIdToCancel)
    }
}

fun CustomerDetails.display() {
    println("Name: $name")
    println("Travel Date: $travelDate")
    println("Source: $source")
    println("Destination: $destination")
    println("Price: $price")
}

fun airlineReservationSystem(action: (Booking) -> Unit) {
    val customerDetails = getCustomerDetailsFromUserInput()
    val booking = Booking(customerDetails)
    action(booking)
}

val paymentProcessor: (String) -> Unit = { paymentMethod ->
    val transactionSystem = TransactionManagementSystem()
    transactionSystem.processPayment(paymentMethod)
}

fun saveBookingToFile(bookingDetails: BookingDetails) {
    val fileName = "booking_${bookingDetails.customerId}.txt"
    val fileContent = """
        Booking Details:
        Name: ${bookingDetails.name}
        Travel Date: ${bookingDetails.travelDate}
        Source: ${bookingDetails.source}
        Destination: ${bookingDetails.destination}
        Price: $${bookingDetails.price}
        Customer ID: ${bookingDetails.customerId}
        
        Terms and Conditions:
        1. Ticket can be cancelled only within 24 hours.
        2. No refunds for cancellations made after 24 hours.
        3. Payment should be made within 30 minutes of booking.
    """.trimIndent()
    File(fileName).writeText(fileContent)
}

fun getCustomerDetailsFromUserInput(): CustomerDetails {
    println("Welcome to Bhindigo!")
    println("Enter customer details:")
    print("Name: ")
    val name = readLine() ?: ""
    print("Travel Date: ")
    val travelDate = readLine() ?: ""
    print("Source (A, B, C): ")
    val source = readLine() ?: ""
    print("Destination (A, B, C): ")
    val destination = readLine() ?: ""
    val price = calculatePrice(source, destination)

    return object : CustomerDetails {
        override val name: String = name
        override val travelDate: String = travelDate
        override val source: String = source
        override val destination: String = destination
        override val price: Double = price
    }
}

fun calculatePrice(source: String, destination: String): Double {
    return when (source + destination) {
        "AB" -> 50.0
        "BC" -> 75.0
        "AC" -> 125.0
        else -> 0.0
    }
}

fun main() {
    airlineReservationSystem { booking ->
        booking.bookTicket()
    }

    println("Choose a payment method (Credit Card, Debit Card, Cash, UPI): ")
    val paymentMethod = readLine() ?: "Credit Card"
    paymentProcessor(paymentMethod)

    val bookingDetails = BookingDetails("", "", "", "", 0.0, 0)
    saveBookingToFile(bookingDetails)
    println("Booking ticket saved to a text file.")
}
