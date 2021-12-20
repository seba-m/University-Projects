using System.ComponentModel.DataAnnotations;
using System.Text.RegularExpressions;

namespace InventarisPro.Modelo.ViewModels
{
    public class VerificarNumeroValido : ValidationAttribute
    {
        public override bool IsValid(object value)
        {
            return value != null && int.TryParse(value.ToString(), out int i) && i >= 0;
        }
    }
}
