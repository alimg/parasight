uniform sampler2D baseImage;
uniform vec2 baseSize;


#define FXAA_REDUCE_MIN   (1.0/ 128.0)
#define FXAA_REDUCE_MUL   (1.0 / 8.0)
#define FXAA_SPAN_MAX     8.0

vec4 applyFXAA(vec2 fragCoord, sampler2D tex)
{
    vec4 color;
    vec2 inverseVP = vec2(1.0 / baseSize.x, 1.0 / baseSize.y);
    vec4 rgbNW = texture2D(tex, fragCoord + (vec2(-1.0, -1.0)) * inverseVP);
    vec4 rgbNE = texture2D(tex, fragCoord + (vec2(1.0, -1.0)) * inverseVP);
    vec4 rgbSW = texture2D(tex, fragCoord + (vec2(-1.0, 1.0)) * inverseVP);
    vec4 rgbSE = texture2D(tex, fragCoord + (vec2(1.0, 1.0)) * inverseVP);
    vec4 rgbM  = texture2D(tex, fragCoord );
    vec4 luma = vec4(0.299, 0.587, 0.114,0.5);
    float lumaNW = dot(rgbNW, luma);
    float lumaNE = dot(rgbNE, luma);
    float lumaSW = dot(rgbSW, luma);
    float lumaSE = dot(rgbSE, luma);
    float lumaM  = dot(rgbM,  luma);
    float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
    float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));
    
    vec2 dir;
    dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
    dir.y =  ((lumaNW + lumaSW) - (lumaNE + lumaSE));
    
    float dirReduce = max((lumaNW + lumaNE + lumaSW + lumaSE) *
                          (0.25 * FXAA_REDUCE_MUL), FXAA_REDUCE_MIN);
    
    float rcpDirMin = 1.0 / (min(abs(dir.x), abs(dir.y)) + dirReduce);
    dir = min(vec2(FXAA_SPAN_MAX, FXAA_SPAN_MAX),
              max(vec2(-FXAA_SPAN_MAX, -FXAA_SPAN_MAX),
              dir * rcpDirMin)) * inverseVP;
      
    vec4 rgbA = 0.5 * (
        texture2D(tex, fragCoord  + dir * (1.0 / 3.0 - 0.5)) +
        texture2D(tex, fragCoord  + dir * (2.0 / 3.0 - 0.5)));
    vec4 rgbB = rgbA * 0.5 + 0.25 * (
        texture2D(tex, fragCoord  + dir * -0.5) +
        texture2D(tex, fragCoord  + dir * 0.5));

    float lumaB = dot(rgbB, luma);
    if ((lumaB < lumaMin) || (lumaB > lumaMax))
        color = rgbA;
    else
        color = rgbB;
    return color;
}

void main()
{
    gl_FragColor = s;*/
	
	gl_FragColor=applyFXAA(vec2(gl_TexCoord[0]),baseImage);

    //Gamma correction
    //gl_FragColor.rgb = pow(s.rgb, vec3(1.0 / 0.56));
    
}
