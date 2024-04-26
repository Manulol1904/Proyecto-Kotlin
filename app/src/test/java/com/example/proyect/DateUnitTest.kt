import com.example.proyect.date.currentDate
import org.junit.Assert
import org.junit.Test

class DateTest {
    @Test
    fun testCurrentDate() {
        val expectedDateFormat = "yyyy-MM-dd"
        val currentDate = currentDate

        Assert.assertNotNull(currentDate)
        Assert.assertEquals(expectedDateFormat.length.toLong(), currentDate.length.toLong())
    }
}
