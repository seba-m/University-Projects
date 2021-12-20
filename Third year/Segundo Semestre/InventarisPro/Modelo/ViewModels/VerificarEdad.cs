using System.ComponentModel.DataAnnotations;
using System.Text.RegularExpressions;

namespace InventarisPro.Modelo.ViewModels
{
    public class VerificarEdad : ValidationAttribute
    {
        private readonly int _Limit;
        public VerificarEdad(int Limit)
        {
            _Limit = Limit;
        }
        protected override ValidationResult IsValid(object value, ValidationContext validationContext)
        {
            DateTime bday = DateTime.Parse(value.ToString());
            DateTime today = DateTime.Today;
            int age = today.Year - bday.Year;
            if (bday > today.AddYears(-age))
            {
                age--;
            }
            if (age < _Limit)
            {
                var result = new ValidationResult("Edad mínima: " + _Limit + " años.");
                return result;
            }


            return null;

        }
    }
}
