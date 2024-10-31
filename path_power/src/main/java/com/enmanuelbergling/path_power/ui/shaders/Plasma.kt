package com.enmanuelbergling.path_power.ui.shaders

import android.graphics.RuntimeShader
import androidx.annotation.FloatRange
import org.intellij.lang.annotations.Language

@Language("AGSL")
val HSLColorSpaceShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    uniform float alpha;
    
    const float PI =3.14159265358979;

    // Convert HSL colorspace to RGB. http://en.wikipedia.org/wiki/HSL_and_HSV
    float3 HSLtoRGB(in float3 c)
    {
    	float3 rgb = clamp(abs(mod(c.x+float3(0.,4.,2.),6.)-3.)-1.,0.,1.);
    	return c.z+c.y*(rgb-0.5)*(1.-abs(2.*c.z-1.));
    }

    float3 HSL2RGB_CubicSmooth(in float3 c)
    {
        float3 rgb = clamp(abs(mod(c.x+float3(0.,4.,2.),6.)-3.)-1.,0.,1.);
        rgb = rgb*rgb*(3.0-2.0*rgb); // iq's cubic smoothing.
        return c.z+ c.y*(rgb-0.5)*(1.-abs(2.*c.z-1.));
    }

    float4 main(  in float2 fragCoord )
    {
    	float2 uv = (-1.+2.*fragCoord.xy/resolution.xy)*float2(resolution.x/resolution.y,1.);
    	float fAngle = time*0.4;
    	float h = atan(uv.x,uv.y) - fAngle;
    	float x = length(uv);
    	float a = -(0.6+0.2*sin(time*3.1+sin((time*0.8+h*2.0)*3.0))*sin(time+h));
    	float b = -(0.8+0.3*sin(time*1.7+sin((time+h*4.0))));
    	float c = 1.25+sin((time+sin((time+h)*3.0))*1.3)*0.15;
    	float l = a*x*x + b*x + c;
    	//float3 hsl_standard = HSLtoRGB(float3(h*3./PI,1.,l));
    	float3 hsl_cubic = HSL2RGB_CubicSmooth(float3(h*3.0/PI,1.,l));
    	float4 fragColor = float4(hsl_cubic,alpha);
    
        //value doesn't change
        if( fragColor == float4(0., 0., 0., 1.) ){
            return float4(backgroundColor.rgb, 1.0);
        } else{
            return fragColor;
        }
    }
""".trimIndent()

@Language("AGSL")
val ColorWarpShader = """ //the one that comes towards to you
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    uniform float alpha;
    
    const bool WARP =true;
    const float BALLS =10.;
    const float CONTRAST= 3;
    const float GLOW =.1;
    const float ORB_SIZE =0.492519;
    const float PI =3.14159265359;

    float2 kale(float2 uv, float2 offset, float splits) {
      float angle = atan(uv.y, uv.x);
      angle = ((angle / PI) + 1.0) * 0.5;
      angle = mod(angle, 1.0 / splits) * splits;
      angle = -abs(2.0 * angle - 1.0) + 1.0;
      float y = length(uv);
      angle = angle * (y);
      return float2(angle, y) - offset;
    }

    float4 main ( in float2 fragCoord) {
      float2 uv = 2. * fragCoord/resolution.xy - 1.;
      uv.x *= resolution.x / resolution.y;
      uv *= 2.2;
      float4 fragColor = float4(0.);
      float dist = distance(uv, float2(0.));
      uv = WARP ? uv * kale(uv, float2(0.), 2.) : uv;
      for (float i = 0.; i < BALLS; i++) {
        float t = time/2. - i * PI / BALLS * cos(time / max(i, 0.0001));
        float2 p = float2(cos(t), sin(t)) / sin(i / BALLS * PI / dist + time);
        float3 c = cos(float3(0, 5, -5) * PI * 2. / PI + PI * (time / (i+1.) / 5.)) * (GLOW) + (GLOW);
        fragColor += float4(float3(dist * .35 / length(uv - p * ORB_SIZE) * c), 1.0);
      }
      fragColor.xyz = pow(fragColor.xyz, float3(CONTRAST));
      
      //value doesn't change
        if( fragColor == float4(0.) ){
            return float4(backgroundColor.rgb, alpha);
        } else{
            return fragColor;
        }
    }
""".trimIndent()

@Language("AGSL")
val HotPlasmaShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    uniform float alpha;
    
    float4 main(  in float2 fragCoord )
    {
    	float2 p=(2.0*fragCoord.xy-resolution.xy)/max(resolution.x,resolution.y);
    	for(int i=1;i<10;i++)
    	{
    		float2 newp=p;
    		newp.x+=0.6/float(i)*sin(float(i)*p.y+time+0.3*float(i))+1.0;
    		newp.y+=0.6/float(i)*sin(float(i)*p.x+time+0.3*float(i+10))-1.4;
    		p=newp;
    	}
    	float3 col=float3(1.0,1.0-(sin(p.y)),sin(p.x+p.y));
    	float4 fragColor=float4(col*alpha, alpha);
        return fragColor;
    }
""".trimIndent()

@Language("AGSL")
val LavavaLampaShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    uniform float alpha;
    
    float4 main(  in float2 fragCoord )
    {
        // Normalized pixel coordinates (from 0 to 1)
        float2 uv = fragCoord/resolution.xy-0.5;
        float2 offset = float2(sin(time*2.)*0.5, cos(time*2.)*0.5);
        uv.x -= offset.x;
        uv.y -= offset.y;
        
        // Time varying pixel color
        float3 col =0.5+0.9*sin(-time+(uv.x*uv.x)+(uv.y*uv.y)+float3(8,1,3))
        *cos(time+(uv.y*uv.x)+(uv.y*uv.y)+float3(5,1,6))
        +0.5*cos(time+(uv.y*uv.x)+(uv.y*uv.y)+float3(1,2,3))
        *cos(time+(uv.y*uv.x)
        +(uv.y*uv.y)+float3(13,5,8))*cos(time+(uv.y*uv.x)
        +(uv.y*uv.y)+float3(1,2,1))+0.5*cos(time+(uv.y*uv.x)+(uv.y*uv.y)+float3(1,2,3));

        // Output to screen
        float4 fragColor = float4(col*alpha,alpha);
        return fragColor;
    }
""".trimIndent()

@Language("AGSL")
val KLKSimplePlasmaShader = """ //most artistic an colorful one
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    uniform float alpha;
    
    float4 main(  in float2 fragCoord )
    {
    	float time=time*1.0;
    	float2 uv = (fragCoord.xy / resolution.xx-0.5)*8.0;
        float2 uv0=uv;
    	float i0=1.0;
    	float i1=1.0;
    	float i2=1.0;
    	float i4=0.0;
    	for(int s=0;s<7;s++)
    	{
    		float2 r;
    		r=float2(cos(uv.y*i0-i4+time/i1),sin(uv.x*i0-i4+time/i1))/i2;
            r+=float2(-r.y,r.x)*0.3;
    		uv.xy+=r;
            
    		i0*=1.93;
    		i1*=1.15;
    		i2*=1.7;
    		i4+=0.05+0.1*time*i1;
    	}
        float r=sin(uv.x-time)*0.5+0.5;
        float b=sin(uv.y+time)*0.5+0.5;
        float g=sin((uv.x+uv.y+sin(time*0.5))*0.5)*0.5+0.5;
    	float4 fragColor = float4(r,g,b,1.);
        return fragColor*alpha;
    }
""".trimIndent()

@Language("AGSL")
val LavaPlasmaShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    uniform float alpha;
    
    const float PI =3.14159265359;
    const float EXP =2.71828182846;

    float w1 = 3.0;
    float w2 = 1.0;
    float w3 = 20.0;
    float A = 1.0;
    float R = 3.0;

    float horizontal(in float2 xy, float t)	{
        float v = cos(w1*xy.x + A*t);
    	return v;
    }
        
    float diagonal(in float2 xy, float t)	{
        float v = cos(w2*(xy.x*cos(t) + 5.0*xy.y*sin(t)) + A*t);
        return v;
    }
    float radial(in float2 xy, float t)	{
        float x = 0.3*xy.x - 0.5 + cos(t);
        float y = 0.3*xy.y - 0.5 + sin(t*0.5);
        float v = sin(w3*sqrt(x*x+y*y+1.0)+A*t);
        return v;
    }

    float map(float a,float b,float c,float d,float x) {
        return ((x-a)*(d-c)/(b-a))+c;
    }

    float log_map(float a,float b,float c,float d,float x) {
        float x1 = map(a,b,1.0,EXP,x);
        return log(x1)*(d-c)+c;
    }

    float4 main(  in float2 fragCoord )	{
        float t = time;
    	float2 xy = fragCoord.xy / resolution.xy;
        float v = horizontal(xy,t);
        v += diagonal(xy,t);
        v += radial(xy,t);
        v /= 3.0;
        float r = map(-1.0,1.0,   0.75,1.0,sin(PI*v));
        float g = map(-1.0,1.0,   0.0,0.8,sin(PI*v));
        g += log_map(-1.0,1.0,   0.0,0.1,cos(PI*v));
        float b = map(-1.0,1.0,   0.0,0.1,sin(PI*v));
        float4 fragColor = float4(pow(r,R),pow(g,R),pow(b,R),1.);
        return fragColor * alpha;
    }
""".trimIndent()

@Language("AGSL")
val GoldenFlowShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    uniform float alpha;
    
    float4 main( in float2 fragCoord )
    {
        float2 q=7.0*(fragCoord.xy-0.5*resolution.xy)/max(resolution.x,resolution.y);
    	
    	for(float i=1.0;i<40.0;i+=0.93)
    	{
    		float2 o=q;
    		o.x+=(0.5/i)*cos(i*q.y+time*0.297+0.03*i)+1.3;		
    		o.y+=(0.5/i)*cos(i*q.x+time*0.414+0.03*(i+10.0))+1.9;
    		q=o;
    	}

    	float3 col=float3(0.5*sin(3.0*q.x)+0.5,0.5*sin(3.0*q.y)+0.5,sin(1.3*q.x+1.7*q.y));
        float f=0.43*(col.x+col.y+col.z);

    	float4 fragColor=float4(f+0.6,0.2+0.75*f,0.2,1.);
        return fragColor * alpha;
    }
""".trimIndent()

fun goldenShader(
    @FloatRange(.0, 1.0) alpha: Float = 1f,
) = RuntimeShader(GoldenFlowShader).apply {
    setFloatUniform("alpha", alpha)
}

fun lavaShader(
    @FloatRange(.0, 1.0) alpha: Float = 1f,
) = RuntimeShader(LavaPlasmaShader).apply {
    setFloatUniform("alpha", alpha)
}

fun artisticPlasma(
    @FloatRange(.0, 1.0) alpha: Float = 1f,
) = RuntimeShader(KLKSimplePlasmaShader).apply {
    setFloatUniform("alpha", alpha)
}

fun hotPlasma(
    @FloatRange(.0, 1.0) alpha: Float = 1f,
) = RuntimeShader(HotPlasmaShader).apply {
    setFloatUniform("alpha", alpha)
}