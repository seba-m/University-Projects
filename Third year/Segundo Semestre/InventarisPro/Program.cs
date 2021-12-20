using Amazon;
using Amazon.S3;
using InventarisPro.Modelo;
using InventarisPro.Proveedores;
using InventarisPro.Proveedores.Repositorios;
using InventarisPro.Services;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllersWithViews();
builder.Services.AddDbContext<ApplicationDbContext>(options =>
{
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection"));
});
builder.Services.AddHttpContextAccessor();

builder.Services.AddSingleton<IAmazonS3>(p => {
    var config = new AmazonS3Config
    {
        RegionEndpoint = RegionEndpoint.SAEast1,
    };
    return new AmazonS3Client(builder.Configuration.GetConnectionString("AWSS3:AccessKey"), builder.Configuration.GetConnectionString("AWSS3:SecretKey"), config);
});
builder.Services.AddTransient<ServicesAWSS3>();

builder.Services.AddScoped<INegocioRepository, NegocioRepository>();
builder.Services.AddScoped<IProductoRepository, ProductoRepository>();
builder.Services.AddScoped<ITrabajadorRepository, TrabajadorRepository>();
builder.Services.AddScoped<ICategoriaRepository, CategoriaRepository>();
builder.Services.AddScoped<IAdministradorSesion, AdministradorSesion>();
builder.Services.AddScoped<IDetalleRepository, DetalleRepository>();
builder.Services.AddScoped<IVentaRepository, VentaRepository>();
builder.Services.AddScoped<IHistorialRepository, HistorialRepository>();
builder.Services.AddScoped<IOptimizacionRepository, OptimizacionRepository>();

builder.Services.AddAuthentication(options =>
{
    options.DefaultScheme = CookieAuthenticationDefaults.AuthenticationScheme;
    options.DefaultSignInScheme = CookieAuthenticationDefaults.AuthenticationScheme;
    options.DefaultSignOutScheme = CookieAuthenticationDefaults.AuthenticationScheme;
}).AddCookie(CookieAuthenticationDefaults.AuthenticationScheme, (options) =>
{
    options.LoginPath = "/Account/Login";
    options.LogoutPath = "/Account/Logout";
});

builder.Services.Configure<CookiePolicyOptions>(options =>
{
    options.CheckConsentNeeded = context => true;
    options.MinimumSameSitePolicy = SameSiteMode.None;
});

var app = builder.Build();

app.UseDeveloperExceptionPage();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error/{0}");
    app.UseStatusCodePagesWithReExecute("/Error/{0}");
    app.UseHsts();
} else
{
    app.UseStatusCodePagesWithReExecute("/Error/{0}");
    app.UseDeveloperExceptionPage();
}

app.UseHttpsRedirection();
//app.UseDefaultFiles();
app.UseStaticFiles();

app.UseCookiePolicy();

app.UseAuthentication();

app.UseRouting();

app.UseAuthorization();

app.UseEndpoints(endpoints =>
{
    endpoints.MapControllers();
    //endpoints.MapRazorPages();
});

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
