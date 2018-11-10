using System;
using System.Linq;
using System.Runtime.Serialization;
using Microsoft.AspNetCore.Mvc;
using System.IO;
using MessagePack;
using Utf8Json;


namespace server.Controllers
{
    [Route("api/[controller]")]
    public class MessagePackController : Controller
    {

        [HttpPost]
        public ActionResult Post()
        {
            if (Request.Body == null)
                return NotFound();

            using (var ms = new MemoryStream())
            {
                Request.Body.CopyTo(ms);
                var bytes = ms.ToArray();
                var json = MessagePackSerializer.ToJson(bytes);
                var sample = JsonSerializer.Deserialize<Sample>(json);
                return Ok($"Recieved {bytes.Length} bytes. Deserialized object is [{sample.ToString()}]");
            }
        }
    }
}

public enum Color
{
    Red,
    Yellow,
    Blue,
    Green
}

public class Sample
{

    [DataMember(Name = "number")]
    public int Number { get; set; }

    [DataMember(Name = "flag")]
    public bool Flag { get; set; }

    [DataMember(Name = "color")]
    public Color Color { get; set; }

    [DataMember(Name = "description")]
    public string Description { get; set; }

    public override string ToString()
        => $"Number: {Number}, Flag: {Flag}, Color: {Color.ToString()}, Description: {Description}";
}
