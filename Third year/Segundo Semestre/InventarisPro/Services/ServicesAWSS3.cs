using Amazon.S3;
using Amazon.S3.Model;
using InventarisPro.Modelo.Entidades;
using InventarisPro.Modelo.ViewModels.Producto;
using System.Net;

namespace InventarisPro.Services
{
    public class ServicesAWSS3
    {
        private readonly string bucketName;
        private readonly IAmazonS3 awsclient;

        public ServicesAWSS3(IAmazonS3 amazonS3, IConfiguration configuration)
        {
            awsclient = amazonS3;
            bucketName = configuration["AWSS3:BucktName"];
        }

        public async Task<List<string>> GetAllFiles()
        {
            ListVersionsResponse listVersions = await awsclient.ListVersionsAsync(bucketName);
            return listVersions.Versions.Select(x => x.Key).ToList();
        }

        public async Task<bool> EliminarFoto(Producto p)
        {
            try
            {
                var deleteObjectRequest = new DeleteObjectRequest
                {
                    BucketName = bucketName,
                    //key es la ubicación donde se encuentra la foto
                    Key = string.Format($"fotos/producto/{p.IdNegocio}/{p.IdProducto}/foto.png")
                };

                DeleteObjectResponse response = await awsclient.DeleteObjectAsync(deleteObjectRequest);
                return (response.HttpStatusCode == HttpStatusCode.OK);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return false;
            }
        }

        public async Task<bool> EliminarFoto(Trabajador t)
        {
            try
            {
                var deleteObjectRequest = new DeleteObjectRequest
                {
                    BucketName = bucketName,
                    //key es la ubicación donde se encuentra la foto
                    Key = string.Format($"fotos/trabajador/{t.IdNegocio}/{t.Rut}/foto.png"),
                };

                DeleteObjectResponse response = await awsclient.DeleteObjectAsync(deleteObjectRequest);
                return (response.HttpStatusCode == HttpStatusCode.OK);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return false;
            }
        }

        public async Task<bool> SubirFotoProducto(Stream stream, Producto p)
        {
            PutObjectRequest request = new()
            {
                InputStream = stream,
                //key es la ubicación donde se encuentra la foto
                Key = string.Format($"fotos/producto/{p.IdNegocio}/{p.IdProducto}/foto.png"),
                BucketName = bucketName
            };
            try
            {
                PutObjectResponse response = await awsclient.PutObjectAsync(request);
                return (response.HttpStatusCode == HttpStatusCode.OK);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return false;
            }
        }

        public async Task<bool> SubirFotoTrabajador(Stream stream, Trabajador t)
        {
            PutObjectRequest request = new()
            {
                InputStream = stream,
                //key es la ubicación donde se encuentra la foto
                Key = string.Format($"fotos/trabajador/{t.IdNegocio}/{t.Rut}/foto.png"),
                BucketName = bucketName
            };
            try
            {
                PutObjectResponse response = await awsclient.PutObjectAsync(request);
                return (response.HttpStatusCode == HttpStatusCode.OK);
            }
            catch
            {
                return false;
            }
        }
    }
}
