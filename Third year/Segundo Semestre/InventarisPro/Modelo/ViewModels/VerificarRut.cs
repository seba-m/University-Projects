using System.ComponentModel.DataAnnotations;
using System.Text.RegularExpressions;

namespace InventarisPro.Modelo.ViewModels
{
    public class VerificarRut : ValidationAttribute
    {
        public override bool IsValid(object? value)
        {
            if (value == null) return false;

            string? rut = Convert.ToString(value);

            if (string.IsNullOrWhiteSpace(rut)) return false;

            return VerificarFormatoRut(rut) && VerificarExistenciaRut(rut);
        }
        private bool VerificarFormatoRut(string rut)
        {
            if (!(rut.Length <= 12 && rut[0] != '0')) return false;
            return Regex.Match(rut, "\\b(\\d{1,3}(?:(\\.?)\\d{3}){2}(-?)[\\dkK])\\b").Success;
        }
        public bool VerificarExistenciaRut(string rut)
        {
            rut = rut.Replace(".", "").Replace("-","");
            char digitoVerificador = rut[rut.Length - 1];
            rut = rut.Substring(0, rut.Length - 1);
            int multiplicador = 2;
            int suma = 0;
            for (int i = rut.Length - 1; i >= 0; i--)
            {
                suma += (int)char.GetNumericValue(rut[i]) * multiplicador;
                multiplicador += 1;
                if (multiplicador == 8) multiplicador = 2;
            }
            int division = suma / 11;
            int multiplicado = division * 11;
            int resta = Math.Abs(suma - multiplicado);
            if (Math.Abs((11 - resta)) == (int)char.GetNumericValue(digitoVerificador))
            {
                return true;
            }
            else if ((digitoVerificador == 'K' || digitoVerificador == 'k') && Math.Abs((11 - resta)) == 10)
            {
                return true;
            }
            else if (digitoVerificador == '0' && Math.Abs((11 - resta)) == 11)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
